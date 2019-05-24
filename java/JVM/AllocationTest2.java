class AllocationTest2 {

    private static final int _1MB = 1024 * 1024;

    public static void main(String[] args) {
        byte[] allocation1 = new byte[4 * _1MB];
    }
    /**
     * java -Xms20m -Xmx20m -Xmn10m -XX:+UseSerialGC -XX:+PrintGCDetails -XX:PretenureSizeThreshold=3145728 AllocationTest2
     * Heap
 def new generation   total 9216K, used 835K [0x00000000fec00000, 0x00000000ff600000, 0x00000000ff600000)
  eden space 8192K,  10% used [0x00000000fec00000, 0x00000000fecd0f18, 0x00000000ff400000)
  from space 1024K,   0% used [0x00000000ff400000, 0x00000000ff400000, 0x00000000ff500000)
  to   space 1024K,   0% used [0x00000000ff500000, 0x00000000ff500000, 0x00000000ff600000)
 tenured generation   total 10240K, used 4096K [0x00000000ff600000, 0x0000000100000000, 0x0000000100000000)
   the space 10240K,  40% used [0x00000000ff600000, 0x00000000ffa00010, 0x00000000ffa00200, 0x0000000100000000)
     */
    /**
     * 分析一下，大对象阈值设定的是3MB，对于4MB的大对象，直接分配到老年代
     */
}