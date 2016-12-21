package com.kade_c.hearth;

/**
 * Interface for locking the Navigation Drawer's access in certain Fragments.
 */
public class DrawerLocker {
    public interface DrawerLockerItf {
        public void setDrawerEnabled(boolean enabled);
    }
}
