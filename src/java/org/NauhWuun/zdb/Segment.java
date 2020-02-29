package org.NauhWuun.zdb;

import org.NauhWuun.zdb.Cache.ARC.ARCache;
import org.NauhWuun.zdb.Cache.Cached.KEY;
import org.NauhWuun.zdb.Cache.Cached.VALUE;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Segment
{
    /**
     * Event Segment Max Size Is 32M
     */
    private static final int DEFAULTSEGMENTSIZE = (2 << 4) * 1024 * 1024 * 1;

    private long id = 0, count, offset;
    private ARCache<KEY, VALUE> cached;
    private boolean unnsed = true;
    private File dir;

    public Segment(final long id, final long offset) {
        this.id = id;
        this.offset = offset;
        this.unnsed = false;
    }

    public Segment Build() {
        this.dir = new File(".\\" + id + ".segment");

        if (! dir.exists()) {
            cached = new ARCache<>(DEFAULTSEGMENTSIZE);

            try {
                this.dir.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            cached = fetch();
        }
        
        return this;
    }

    public void add(final KEY key, final VALUE value) {
        cached.set(key, value);
        count++;
    }

    public void flushDisk() {
        persist(cached);
    }

	private ARCache<KEY, VALUE> fetch() {
        ObjectInputStream stream = null;
        
		try {
            stream = new ObjectInputStream(new FileInputStream(dir));
			return (ARCache<KEY, VALUE>) stream.readObject();
		} catch (IOException | ClassNotFoundException e) {
			return null;
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private boolean persist(ARCache<KEY, VALUE> caches) {
        ObjectOutputStream stream = null;

		try {
            stream = new ObjectOutputStream(new FileOutputStream(dir));
			stream.writeObject(caches);
		} catch (IOException e) {
			e.getMessage();
        } finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
        }
        
		return true;
	}

    public VALUE get(KEY key) {
        return cached.get(key);
    }

    public ARCache<KEY, VALUE> getCached() {
        return this.cached;
    }

    public final long getCount() {
        return this.count;
    }

    public final long getId() {
        return this.id;
    }

    public final long getOffset() {
        return this.offset;
    }

    public final boolean getUnnsed() {
        return this.unnsed;
    }
}