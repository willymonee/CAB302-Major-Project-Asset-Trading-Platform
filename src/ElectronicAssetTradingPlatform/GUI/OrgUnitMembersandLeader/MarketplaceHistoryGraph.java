package ElectronicAssetTradingPlatform.GUI.OrgUnitMembersandLeader;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class MarketplaceHistoryGraph extends JComponent {
    private ArrayList<String> y2values = new ArrayList<>();
    private String firsty1value;

    public MarketplaceHistoryGraph(Object[][] data, int width, int height) {
        ArrayList<Float> y_values = new ArrayList<>();
        ArrayList<Date> x_values = new ArrayList<>();
        for (Object[] row : data) {
            x_values.add((Date) row[0]);
            y_values.add((float) row[1]);
        }

        // Get ceiling/floor
        float min_y = Collections.min(y_values);
        float max_y = Collections.max(y_values);
        long min_x = Collections.min(x_values).getTime();
        long max_x = Collections.max(x_values).getTime();

        // Reduce min to 0
        float diff_x = max_x - min_x;
        float diff_y = max_y - min_y;

        firsty1value = String.valueOf(y_values.get(0));
        for (int i = 0; i < data.length - 1; i++) {
            float y2 = y_values.get(i + 1);

            float value1 = 1 - (y_values.get(i) - min_y) / diff_y;
            float value2 = 1 - (y2 - min_y) / diff_y;

            float date1 = (x_values.get(i).getTime() - min_x) / diff_x;
            float date2 = (x_values.get(i + 1).getTime() - min_x) / diff_x;

            addLine((int) (date1 * (width - 50)), (int) (value1 * (height - 50)),
                    (int) (date2 * (width - 50)), (int) (value2 * (height - 50)),
                    String.valueOf(y2));
        }

        addFirstLastDate(x_values.get(0).toString(), x_values.get(x_values.size()-1).toString());

        setPreferredSize(new Dimension(width, height));

        repaint();
    }

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

    private final LinkedList<Line> lines = new LinkedList<>();

    public void addLine(int x1, int y1, int x2, int y2, String y2value) {
        lines.add(new Line(x1,y1,x2,y2));
        y2values.add(y2value);
    }

    private String firstDate;
    private String lastDate;
    public void addFirstLastDate(String firstDate, String lastDate) {
        this.firstDate = firstDate;
        this.lastDate = lastDate;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // BG
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Draw grid
        g.setColor(Color.LIGHT_GRAY);
        int y = 0;
        while (y < getHeight() - 20) {
            g.drawLine(0, y, getWidth(), y);
            y += 30;
        }
        int x = 0;
        while (x < getWidth() - 20) {
            g.drawLine(x, 0, x, getHeight());
            x += 30;
        }

        // Draw axes label
        g.setColor(Color.BLACK);
        g.drawString("Price", getWidth()-30, getHeight()/2);
        g.drawString("Date", getWidth()/2-10, getHeight());

        // Draw lines
        // First y-value
        g.drawOval(lines.getFirst().x1-3, lines.getFirst().y1-3, 5, 5);

        g.setColor(Color.BLACK);
        g.drawString(firsty1value, lines.getFirst().x1+10, lines.getFirst().y1+12);

        // All
        int i = 0;
        for (Line line : lines) {
            g.setColor(Color.DARK_GRAY);
            g.drawLine(line.x1, line.y1, line.x2, line.y2);

            g.drawOval(line.x1-3, line.y1-3, 5, 5);

            // Erase any text behind
            g.setColor(Color.WHITE);
            g.fillRect(line.x2+10, line.y2+12, 30, -15);

            // Write text
            g.setColor(Color.BLACK);
            g.drawString(y2values.get(i), line.x2+11, line.y2+12);
            i++;
        }

        // Draw dates on x-axis
        g.setColor(Color.BLACK);
        g.drawString(firstDate, 0, getHeight());
        g.drawString(lastDate, getWidth()-65, getHeight());
    }
}
