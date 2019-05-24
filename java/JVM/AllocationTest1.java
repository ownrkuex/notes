class AllocationTest1 {

    private static final int _1MB = 1024 * 1024;

    public static void main(String[] args) {
        byte[] allocation1, allocation2, allocation3;

        allocation1 = new byte[_1MB * 2];
        allocation2 = new byte[_1MB * 2];
        allocation3 = new byte[_1MB * 2];
    }
    /**
     * java -Xms20m -Xmx20m -Xmn10m -XX:SurvivorRatio=2 -XX:+PrintGCDetails -XX:+UseSerialGC AllocationTest1
     * 
     * [GC (Allocation Failure) [DefNew: 4625K->2303K(7680K), 0.0021400 secs] 4625K->4351K(17920K), 0.0021653 secs] [Times: user=0.00 sys=0.01, real=0.00 secs] 
Heap
 def new generation   total 7680K, used 4403K [0x00000000fec00000, 0x00000000ff600000, 0x00000000ff600000)
  eden space 5120K,  41% used [0x00000000fec00000, 0x00000000fee0ce50, 0x00000000ff100000)
  from space 2560K,  89% used [0x00000000ff380000, 0x00000000ff5bfe60, 0x00000000ff600000)
  to   space 2560K,   0% used [0x00000000ff100000, 0x00000000ff100000, 0x00000000ff380000)
 tenured generation   total 10240K, used 2048K [0x00000000ff600000, 0x0000000100000000, 0x0000000100000000)
   the space 10240K,  20% used [0x00000000ff600000, 0x00000000ff800010, 0x00000000ff800200, 0x0000000100000000)
     */
    /**
     * 分析一下，前两个数组可以放到Eden中，第三个数组放到Eden中的时候空间不够，触发
     * Minor GC，Eden中的两个数组转移到Survivor，但是Survivor只能放一个，于是
     * 多出来的那一个就放到老年代中，GC结束后第三个数组放入Eden空间。所以结果是Eden,
     * Survivor和老年代各有一个数组。
     */
}