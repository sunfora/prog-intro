import markup.Emphasis;
import markup.Strong;
import markup.OrderedList;
import markup.Text;
import markup.ListItem;
public class Main {
    public static void main(String[] args) {
        ListItem shit = new ListItem(new Strong(new Text("εγ©")));
        ListItem govno = new ListItem(new Emphasis(new Text("‘«ο€μ!")));
        OrderedList huist = new OrderedList(shit, govno);
        StringBuilder huilder = new StringBuilder();
        huist.toBBCode(huilder);
        System.out.println(huilder);
    }
}