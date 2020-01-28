package java.zjdb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.zjdb.ZCachedKV.ARC.ARCache;
import java.zjdb.ZCachedKV.Cached.KEY;
import java.zjdb.ZCachedKV.Cached.VALUE;

public class Segment
{
    /**
     * Event Segment Max Size Is 64M
     */
    private static final int DEFAULTSEGMENTSIZE = (2 << 5) * 1024 * 1024 * 1;

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
        this.dir = new File(String.valueOf(".\\" + id + ".segment"));

        if (! dir.exists()) {
            cached = new ARCache<KEY, VALUE>(DEFAULTSEGMENTSIZE);

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

    /**
	 * Override, if you knew me, please...
	 */
    public <T> Mapper getMapManager() { return null; }
    public <T> T Sort() { return (T) null; }
    public void run() {}
    public <Return, Data> Return Filter(Data _1) { return (Return) null; }
    public void CloseSegment() {}

    public void flushDisk() {
        persist(cached);
    }

	private ARCache<KEY, VALUE> fetch() {
        ObjectInputStream stream = null;
        
		try {
            stream = new ObjectInputStream(new FileInputStream(dir));
            /**
             * type safe the Serializable, can change it
             */
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
            /**
             * type safe the Serializable, can change it
             */
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