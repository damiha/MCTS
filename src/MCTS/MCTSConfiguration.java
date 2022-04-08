package MCTS;

public class MCTSConfiguration{

    private MCTSMode mode;
    private int iterations;
    private double secondsToThink;

    private int rolloutsPerLeaf = 1;
    private boolean leafParallelization = false;

    public void setFixedIterations(int iterations){
        mode = MCTSMode.FIXED_ITERATIONS;
        this.iterations = iterations;
    }

    public void setFixedTime(double secondsToThink){
        mode = MCTSMode.FIXED_TIME;
        this.secondsToThink = secondsToThink;
    }

    public void setRolloutsPerLeaf(int rolloutsPerLeaf){
        this.rolloutsPerLeaf = rolloutsPerLeaf;
    }

    public void setLeafParallelization(boolean leafParallelization){
        this.leafParallelization = leafParallelization;
    }

    public MCTSMode getMode() {
        return mode;
    }

    public int getIterations() {
        return iterations;
    }

    public double getSecondsToThink() {
        return secondsToThink;
    }

    public int getRolloutsPerLeaf() {
        return rolloutsPerLeaf;
    }

    public boolean isLeafParallelization() {
        return leafParallelization;
    }
}
