import markup.Emphasis;
import markup.Strong;
import markup.OrderedList;
import markup.Text;
import markup.ListItem;
public class Main {
    public static void main(String[] args) {
        ListItem shit = new ListItem(new Strong(new Text("��")));
        ListItem govno = new ListItem(new Emphasis(new Text("����!")));
        OrderedList huist = new OrderedList(shit, govno);
        StringBuilder huilder = new StringBuilder();
        huist.toBBCode(huilder);
        System.out.println(huilder);
    }
}