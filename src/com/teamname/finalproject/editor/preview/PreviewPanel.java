package com.teamname.finalproject.editor.preview;

import com.teamname.finalproject.Tabs;
import com.teamname.finalproject.editor.Map;
import com.teamname.finalproject.editor.preview.previewminimap.PreviewMiniMap;

import javax.swing.*;
import java.awt.*;

public class PreviewPanel extends JPanel {

	private Preview preview;
	private Dimension d;
	private PreviewMiniMap miniMap;
	private int staBlockSize;
    private PreviewEventListener eventListener;

    public PreviewPanel(Map map, Tabs tabs) {
		setLayout(null);
		preview = new Preview( map);
		miniMap = new PreviewMiniMap(preview);
		preview.setMiniMap(miniMap);
		d = Toolkit.getDefaultToolkit().getScreenSize();
		staBlockSize = (int) d.getWidth() / 20;
		miniMap.setLocation((int) d.getWidth() - miniMap.getWidth() - 15 , (int) d.getHeight() - miniMap.getHeight() - 39);
		setSize(d);
		add(miniMap);
		add(preview);
        eventListener = new PreviewEventListener(preview, tabs);
		setVisible(true);

	}

    public Preview getPreview() {
        return preview;
    }
}
