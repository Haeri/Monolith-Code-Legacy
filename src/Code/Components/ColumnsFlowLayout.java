package Code.Components;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

public class ColumnsFlowLayout implements LayoutManager {
    int hGap;
    int vGap;
    public ColumnsFlowLayout() {
        this(2,2);
    }
 
    public ColumnsFlowLayout(int hGap, int vGap) {
        this.hGap=hGap;
        this.vGap=vGap;
    }
    public void addLayoutComponent(String name, Component comp) {
    }
 
    public void removeLayoutComponent(Component comp) {
    }
 
    public Dimension preferredLayoutSize(Container target) {
        synchronized (target.getTreeLock()) {
            Dimension dim = new Dimension(0, 0);
            int count = target.getComponentCount();
            int visibleCount = 0;
            for (int i=0; i<count; i++) {
                if (target.getComponent(i).isVisible()) {
                    visibleCount++;
                }
            }
            Insets insets = target.getInsets();
 
            Dimension maxPref=getCellSize(target);
 
            if (target.getWidth()!=0) {
                int maxWidth = target.getWidth() - (insets.left + insets.right);
                int colCount=maxWidth/(maxPref.width+hGap);
                int row=visibleCount/colCount;
                if (visibleCount % colCount!=0) {
                    row++;
                }
                dim.width += insets.left + insets.right + hGap*(visibleCount-1)+maxPref.width*visibleCount;
                dim.height += insets.top + insets.bottom + row*maxPref.height;
            }
            else {
                dim.width += insets.left + insets.right + hGap*(visibleCount-1)+maxPref.width*visibleCount;
                dim.height += insets.top + insets.bottom + maxPref.height;
            }
            return dim;
        }
    }
 
    private Dimension getCellSize(Container target) {
        Dimension maxPref=new Dimension();
        int count = target.getComponentCount();
        for (int i = 0 ; i < count ; i++) {
            Component m = target.getComponent(i);
            if (m.isVisible()) {
                Dimension d = m.getPreferredSize();
                maxPref.width = Math.max(maxPref.width, d.width);
                maxPref.height = Math.max(maxPref.height, d.height);
            }
        }
 
        return maxPref;
    }
 
    public Dimension minimumLayoutSize(Container target) {
        return preferredLayoutSize(target);
    }
 
    /**
     * Lays out the container.
     *
     * @param target the specified component being laid out
     * @see Container
     * @see       java.awt.Container#doLayout
     */
    public void layoutContainer(Container target) {
        synchronized (target.getTreeLock()) {
            Insets insets = target.getInsets();
            int maxWidth = target.getWidth() - (insets.left + insets.right);
            int count = target.getComponentCount();
            Dimension cellSize=getCellSize(target);
            int x=insets.left;
            int y=insets.top;
            int row=0;
            int colCount=maxWidth/(cellSize.width+hGap);
            int realHGap=colCount>1 ?(maxWidth-colCount*(cellSize.width+hGap))/(colCount-1) : 0;
            for (int i = 0 ; i < count ; i++) {
                Component m = target.getComponent(i);
                if (m.isVisible()) {
                    m.setSize(cellSize.width, cellSize.height);
 
                    m.setLocation(x, y+row*(cellSize.height+vGap));
                    x+=hGap+cellSize.width+realHGap;
                    if (x+cellSize.width>=maxWidth) {
                        row++;
                        x=insets.left;
                    }
                }
            }
        }
    }   
}