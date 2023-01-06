public class Lab7Test{
    public static void main(String[] args) {
        Fahrenheit f = new Fahrenheit(32);
        System.out.println(f);

        Celsius c = new Celsius(100);
        System.out.println(c);
        System.out.println(c.toKelvin());
        System.out.println(c.get() == c.boilingPoint());

        Kelvin k = new Kelvin(273.15);
        System.out.println(k);
        System.out.println(k.toFahrenheit());
        System.out.println(k.get());
        k.set(0);
        System.out.println(k);
        System.out.println(k.toCelsius());

        Temperature[] tarray = new Temperature[3];
        tarray[0] = new Fahrenheit();
        tarray[1] = new Celsius();
        tarray[2] = new Kelvin();
        for(int i = 0; i < tarray.length; i++){
            System.out.println(tarray[i].toCelsius());
        }

        /* Extra Credit
        Celsius cc = new Celsius();
        Fahrenheit ff = new Fahrenheit();
        Kelvin kk = new Kelvin();
        Kelvin kk2 = new Kelvin(100);
        System.out.println(cc.equals(ff));    // True
        System.out.println(cc.equals(kk));    // True
        System.out.println(ff.equals(kk));    // True
        System.out.println(kk.equals(kk2));   // False
        */
    }
}
