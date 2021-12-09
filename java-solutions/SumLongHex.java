public class SumLongHex {
   public static void main(String[] args) {
      long total = 0L;

      for(int i = 0; i < args.length; ++i) {
         total += sumFromString(args[i]);
      }

      System.out.println(total);
   }

   public static boolean isHex(String str) {
      if (str.length() < 3) {
         return false;
      } else {
         return str.charAt(1) == 'x';
      }
   }

   public static long decodeLong(String str) {
      str = str.toLowerCase();
      return isHex(str) ? Long.parseUnsignedLong(str.substring(2), 16) : Long.parseLong(str);
   }

   public static long sumFromString(String str) {
      int len = str.length();
      if (len == 0) {
         return 0L;
      } else {
         long sum = 0L;
         int white = 0;

         for(int var5 = 0; var5 < len; ++var5) {
            char var6 = str.charAt(var5);
            if (!Character.isWhitespace(var6)) {
               ++white;
            } else if (white > 0) {
               sum += decodeLong(str.substring(var5 - white, var5));
               white = 0;
            }
         }

         if (white > 0) {
            sum += decodeLong(str.substring(len - white));
         }

         return sum;
      }
   }
}