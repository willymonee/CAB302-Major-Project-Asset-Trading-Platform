package ElectronicAssetTradingPlatform.GUI.OrgUnitMembersandLeader;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class MarketplaceHistoryGraph extends JComponent {
    private ArrayList<String> y2values = new ArrayList<>();

    //From https://stackoverflow.com/questions/5801734/how-to-draw-lines-in-java
    private static class Line{
        final int x1;
        final int y1;
        final int x2;
        final int y2;

        public Line(int x1, int y1, int x2, int y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }
    }

    private final LinkedList<Line> lines = new LinkedList<Line>();

    public void addLine(int x1, int y1, int x2, int y2, String y2value) {
        lines.add(new Line(x1,y1,x2,y2));
        y2values.add(y2value);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int i = 0;
        for (Line line : lines) {
            g.setColor(Color.DARK_GRAY);
            g.drawLine(line.x1, line.y1, line.x2, line.y2);

            g.setColor(Color.DARK_GRAY);
            g.drawOval(line.x1-3, line.y1-3, 5, 5);

            g.setColor(Color.BLACK);
            g.drawString(y2values.get(i), line.x2+10, line.y2+12);
            i++;
        }
    }
}
