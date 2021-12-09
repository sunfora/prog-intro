public class Sum {
   public static void main(String[] args) {
      int total = 0;
      for(int i = 0; i < args.length; ++i) {
         total += sumFromString(args[i]);
      }
      System.out.println(total);
   }

   public static int sumFromString(String str) {
      int len = str.length();
      if (len == 0) {
         return 0;
      } else {
         int sum = 0;
         int white = 0;
         for(int i = 0; i < len; ++i) {
            char c = str.charAt(i);
            if (!Character.isWhitespace(c)) {
               ++white;
            } else if (white > 0) {
               sum += Integer.parseInt(str.substring(i - white, i));
               white = 0;
            }
         }
         if (white > 0) {
            sum += Integer.parseInt(str.substring(len - white));
         }
         return sum;
      }
   }
}