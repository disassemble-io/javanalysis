package samples;

/**
 * @author Tyler Sedlar
 * @since 2/1/16
 */
public class Sample {

    static double u = 1, i = 0;
    static boolean h = false;

    static double math(int var0, int var1) {
//        System.out.println((double) var0);
//        System.out.println(((var0 * var1) ^ 2) * var1);
//        System.out.println((7L * var0) + ((6.5F / var1)));
        u = (7L * var0) + ((6.5F / var1)) + (7.0F / var0) - (8.0F / var0) / 2;
        u /= 2;
        return u;
//        System.out.println(((30 * var1) + 5) + var0);
//        System.out.println((((var0 * var1) | var0) ^ var0) * (double) ((var1) - var0));

        // make mathematical instructions into their own statement

//        getstatic java/lang/System.out:java.io.PrintStream
//        bipush 30
//        iload1
//        imul                         // var 1
//        iconst_5
//        iadd                         // var 2
//        iload0 // reference to arg0
//        iadd                         // var 3
//        invokevirtual java/io/PrintStream println((I)V);

//        v1 = (var1 * 30)
//        v2 = (5 + v1)
//        v3 = (var0 + v2);
//        System.out.println(v3);
    }

    public static void tableswitch() {
        switch ((int) u) {
            case 1: {
                System.out.println("A");
                break;
            }
            case 2: {
                System.out.println("B");
                break;
            }
            default: {
                System.out.println("C");
                break;
            }
        }
    }

    static final void bg(int var0, int var1, int var2) {
        try {
            if (u < 2) {
                if (var2 <= -245436626) {
                    throw new IllegalStateException();
                }
                if (i == 0 && !h) {
                    System.out.println("A");
                    if (var2 <= -245436626) {
                        return;
                    }
                    return;
                }
            }
        } catch (RuntimeException var6) {
            var6.printStackTrace();
        }
    }

    public static void main(String[] args) {
        bg(1, 2, 9);
        math((int) u, (int) i);
    }
}