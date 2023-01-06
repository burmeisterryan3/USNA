public class Lion
{
    private double weight;
    private double health;
    private double intelligence;

    public Lion(double weight, double health, double intelligence)
    {
        this.weight = weight;
        this.health = health;
        this.intelligence = intelligence;
    }
    public String toString()
    {
        String lion = "Lion " + weight + " " + 
                      health  + " " + intelligence +
            " = " + this.computeFitness();
        return lion;
    }
    private double computeFitness()
    {
        return (health + (0.5*weight) + (1.5*intelligence));
    }

    public boolean isMoreFitThan(Lion challenger)
    {
        return this.computeFitness() > challenger.computeFitness();
    }
}