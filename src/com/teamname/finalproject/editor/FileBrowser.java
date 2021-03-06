package com.teamname.finalproject.editor;

import com.teamname.finalproject.game.level.MapBuffer;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;

public class FileBrowser {

    private JFileChooser browser;
    private String directory;
    private String mapName;
    private MapHandler mapHandler;
    private double time;
    private boolean disableWarningMessage;
    private boolean exitAfterSave;
    private boolean saved;
    private FileNameExtensionFilter fileFormats;
    private MapBuffer gameMapHandler;

    public FileBrowser(String directory, MapHandler mapHandler, MapBuffer gameMapHandler) {
        saved = true;
        this.gameMapHandler = gameMapHandler;
        LookAndFeel previousLF = UIManager.getLookAndFeel();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            browser = new JFileChooser();
            UIManager.setLookAndFeel(previousLF);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        // Add whatever other settings you want to the method
        this.directory = directory;
        mapName = "";
        this.mapHandler = mapHandler;
        fileFormats = new FileNameExtensionFilter("Map Files '.txt'", "txt");
    }

    public void openMap() {
        browser.setSelectedFile(new File(mapName));
        browser.setDialogTitle("Open Map File");
        browser.setCurrentDirectory(new File(directory));
        browser.setFileFilter(fileFormats);
        int result = browser.showOpenDialog(null);
        switch (result) {
            case JFileChooser.APPROVE_OPTION:
                mapName = browser.getSelectedFile().toString();
                convertFileToMap(browser.getSelectedFile());
                break;
            case JFileChooser.CANCEL_OPTION:

                break;
            case JFileChooser.ERROR_OPTION:

                break;
        }
    }

    public void openGameMap() {
        browser.setSelectedFile(new File(mapName));
        browser.setDialogTitle("Open Map File");
        browser.setCurrentDirectory(new File(directory));
        browser.setFileFilter(fileFormats);
        int result = browser.showOpenDialog(null);
        switch (result) {
            case JFileChooser.APPROVE_OPTION:
                mapName = browser.getSelectedFile().toString();
                convertFileToGameMap(browser.getSelectedFile());
                gameMapHandler.drawseasons();
                break;
            case JFileChooser.CANCEL_OPTION:

                break;
            case JFileChooser.ERROR_OPTION:

                break;
        }
    }

    public void saveMap() {
        browser.setSelectedFile(new File(mapName));
        browser.setDialogTitle("Save Map File");
        browser.setCurrentDirectory(new File(directory));
        browser.setFileFilter(fileFormats);
        int actionDialog = browser.showSaveDialog(null);
        switch (actionDialog) {
            case JFileChooser.APPROVE_OPTION:
                if (browser.getSelectedFile().exists()) {
                    int actionDialog2 = JOptionPane.showConfirmDialog(null, "Replace existing file?");
                    if (actionDialog2 == JOptionPane.NO_OPTION) {
                        saveMap();
                    } else if (actionDialog2 == JOptionPane.YES_OPTION) {
                        mapName = browser.getSelectedFile().toString();
                        quickSave(true);
                    } else if (actionDialog2 == JOptionPane.CANCEL_OPTION) {
                    }
                } else {
                    mapName = browser.getSelectedFile().toString();
                    quickSave(true);
                }
                if (exitAfterSave)
                    System.exit(0);
                break;
            case JFileChooser.CANCEL_OPTION:

                break;
            case JFileChooser.ERROR_OPTION:

                break;
        }
    }

    public void deleteMap() {
        mapHandler.getMap().clearMap();
    }

    public void exit(LinkedList<LinkedList<Tile>> map) {
        if (!saved) {
            int actionDialog = JOptionPane.showConfirmDialog(null, "Would you like to save first?");
            if (actionDialog == JOptionPane.NO_OPTION) {
                System.exit(0);
            } else if (actionDialog == JOptionPane.YES_OPTION) {
                exitAfterSave = true;
                quickSave(false);
            } else if (actionDialog == JOptionPane.CANCEL_OPTION) {
            }
        } else
            System.exit(0);
    }

    public void quickSave(boolean force) {
        if (mapName.length() > 0 || force) {
            convertMapToFile();
            if (exitAfterSave)
                System.exit(0);
        } else
            saveMap();
    }

    private void convertMapToFile() {
        StringBuffer newMap = new StringBuffer();
        newMap.append(mapHandler.getMap().mapToRawString());
        BufferedWriter writer = null;
        try {
            if (mapName.indexOf(".txt") < 0)
                mapName += ".txt";
            writer = new BufferedWriter(new FileWriter(mapName));
            writer.write(newMap.toString());
            saved = true;
        } catch (IOException e) {
        } finally {
            try {
                if (writer != null)
                    writer.close();
            } catch (IOException e) {
            }
        }
    }

    private void convertFileToGameMap(File selectedFile) {
        LinkedList<String> stack = new LinkedList<String>();
        int idLength = Tile.getMyTileStringLength();
        Scanner scan = null;
        int rows = 0;
        int cols = 0;
        boolean countRows = true;
        boolean errorMessage = false;
        // read file content
        try {
            scan = new Scanner(new FileReader(selectedFile));
        } catch (IOException e1) {
        }
        while (scan.hasNextLine()) {
            stack.add(scan.nextLine());
            if (countRows &&
                    stack.get(stack.size() - 1).length() >= idLength) { // = a number
                if (stack.get(stack.size() - 1).substring(0, idLength).matches("-?\\d+"))
                    rows++;
                else
                    errorMessage = true;
            } else
                countRows = false;
        }
        // initialize parameters
        if (!errorMessage && rows > 0) {
            cols = stack.get(0).length() / idLength; // parse according to 0-99
            // contruct new array;
            gameMapHandler.newMap(cols, rows);
            int id = 0;
            int type = 0;
            int image = 0;
            boolean depth = true;
            int depthID = 0;
            String stringNum;
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    if (stack.get(row).length() == cols * idLength) {
                        stringNum = stack.get(row).substring(col * idLength, (col * idLength) + idLength);
                        if (stringNum.matches("-?\\d+")) {
                            id = Integer.parseInt(stringNum.substring(0, 2));// first & second digits
                            type = Integer.parseInt(stringNum.substring(2, 3));// third digit
                            image = Integer.parseInt(stringNum.substring(3, 5));// fourth and fifth digits
                            if (Integer.parseInt(stringNum.substring(5, 6)) == 1) {// sixth digit
                                depth = true;
                            } else {
                                depth = false;
                            }
                            if (image == 16) {
                                image = -1;
                            }
                            depthID = Integer.parseInt(stringNum.substring(6, 8));// seventh and 8th digits
                            if (depthID == 16)
                                depthID = -1;
                            gameMapHandler.setTileInfo(row, col, id, type, image, depth, depthID);
                        } else
                            errorMessage = true;
                    } else
                        errorMessage = true;
                }
            }
        }
        if (errorMessage)
            JOptionPane.showMessageDialog(null, "Error: Corrupt or invalid file!");
        if (!errorMessage) {
            // render the mapbuffer
        }
    }

    private void convertFileToMap(File selectedFile) {
        LinkedList<String> stack = new LinkedList<String>();
        int idLength = Tile.getMyTileStringLength();
        Scanner scan = null;
        int rows = 0;
        int cols = 0;
        boolean countRows = true;
        boolean errorMessage = false;
        // read file content
        try {
            scan = new Scanner(new FileReader(selectedFile));
        } catch (IOException e1) {
        }
        while (scan.hasNextLine()) {
            stack.add(scan.nextLine());
            if (countRows &&
                    stack.get(stack.size() - 1).length() >= idLength) { // = a number
                if (stack.get(stack.size() - 1).substring(0, idLength).matches("-?\\d+"))
                    rows++;
                else
                    errorMessage = true;
            } else
                countRows = false;
        }
        // initialize parameters
        if (!errorMessage && rows > 0) {
            cols = stack.get(0).length() / idLength; // parse according to 0-99
            // contruct new array;
            setSavedState(false);
            mapHandler.setNewMap(cols, rows);
            int id = 0;
            int type = 0;
            int image = 0;
            boolean depth = true;
            int depthID = 0;
            String stringNum;
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    if (stack.get(row).length() == cols * idLength) {
                        stringNum = stack.get(row).substring(col * idLength, (col * idLength) + idLength);
                        if (stringNum.matches("-?\\d+")) {
                            id = Integer.parseInt(stringNum.substring(0, 2));// first & second digits
                            type = Integer.parseInt(stringNum.substring(2, 3));// third digit
                            image = Integer.parseInt(stringNum.substring(3, 5));// fourth and fifth digits
                            if (Integer.parseInt(stringNum.substring(5, 6)) == 1) {// sixth digit
                                depth = true;
                            } else {
                                depth = false;
                            }
                            if (image == 16) {
                                image = -1;
                            }
                            depthID = Integer.parseInt(stringNum.substring(6, 8));// seventh and 8th digits
                            if (depthID == 16)
                                depthID = -1;
                            mapHandler.setTileInfo(row, col, id, type, image, depth, depthID);
                        } else
                            errorMessage = true;
                    } else
                        errorMessage = true;
                }
            }
        }
        if (errorMessage)
            JOptionPane.showMessageDialog(null, "Error: Corrupt or invalid file!");
        if (!errorMessage) {
            mapHandler.drawAgainReload();
        }
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSavedState(boolean saved) {
        this.saved = saved;
    }

    public void changeArrayProperties(boolean clear) {
        LookAndFeel previousLF = UIManager.getLookAndFeel();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            boolean go = true;
            JTextField field1 = new JTextField(mapHandler.getCols() + "");
            JTextField field2 = new JTextField(mapHandler.getRows() + "");
            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Columns (left to right): "));
            panel.add(field1);
            panel.add(new JLabel("Rows (top to bottom): "));
            panel.add(field2);
            int result = JOptionPane.showConfirmDialog(null, panel, "Adjust 2D Map Dimension",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                int rows = (int) Double.parseDouble(field2.getText());
                int cols = (int) Double.parseDouble(field1.getText());
                if (rows > 999)
                    rows = 999;
                if (cols > 999)
                    cols = 999;
                if (rows * cols >= 998001) { // 999*999 = pretty slow performance
                    int i = JOptionPane.showConfirmDialog(null,
                            "This data may take longer than usual to construct!\n" +
                                    "Are you sure you want to continue this process?\n",
                            "",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE);
                    if (i == JOptionPane.NO_OPTION)
                        go = false;
                }
                if (go) {
                    double t = System.currentTimeMillis();
                    if (clear)
                        mapHandler.clearMap();
                    mapHandler.setRowsAndCols(rows, cols, clear);
                    memoryWarning((System.currentTimeMillis() - t) / 1000);
                }
            } else {
            }
            UIManager.setLookAndFeel(previousLF);
        } catch (ClassNotFoundException exception) {
            exception.printStackTrace();
        } catch (InstantiationException exception) {
            exception.printStackTrace();
        } catch (IllegalAccessException exception) {
            exception.printStackTrace();
        } catch (UnsupportedLookAndFeelException exception) {
            exception.printStackTrace();
        }

    }

    public void memoryWarning(double time) {
        LookAndFeel previousLF = UIManager.getLookAndFeel();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            if (time > 0.25) {
                if (!disableWarningMessage) {

                    int i = JOptionPane.showConfirmDialog(null,
                            "Your map data is too large to properly backup!\n" +
                                    "Would you like to turn off the backup system\n" +
                                    "to increase your performance?\n\n" +
                                    "Features such as 'undo' and 'redo' will no\n" +
                                    "longer be available for this session!\n\n" +
                                    "Lag: " + time + " seconds\n",
                            "",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE);
                    if (i == JOptionPane.YES_OPTION) {
                        disableWarningMessage = true;
                        mapHandler.setEnableHistory(false);
                    } else if (i == JOptionPane.NO_OPTION)
                        disableWarningMessage = true;
                    else
                        memoryWarning(time);
                }
            }
            UIManager.setLookAndFeel(previousLF);
        } catch (ClassNotFoundException exception) {
            exception.printStackTrace();
        } catch (InstantiationException exception) {
            exception.printStackTrace();
        } catch (IllegalAccessException exception) {
            exception.printStackTrace();
        } catch (UnsupportedLookAndFeelException exception) {
            exception.printStackTrace();
        }
    }

    public void launchCopyPaste() {
        // formats
        final String[] formats = new String[]{"Java 2D", "Java 1D", "C++ 2D", "C++ 1D", "ActionScript 2D"};
        final String[] compiler = new String[]{
                mapHandler.getMap().mapToJava2dString(),
                mapHandler.getMap().mapToJava1dString(),
                mapHandler.getMap().mapToCPlusPlus2dString(),
                mapHandler.getMap().mapToCPlusPlus1dString(),
                mapHandler.getMap().mapToActionScript()
        };
        final JComboBox box = new JComboBox(formats);
        // text area
        final JTextArea textArea = new JTextArea(mapHandler.getMap().mapToJava2dString());
        textArea.setColumns(32);
        textArea.setRows(8);
        textArea.setLineWrap(false);
        textArea.setSize(textArea.getPreferredSize().width, 1);
        final JScrollPane scroll = new JScrollPane(textArea,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        // copy button settings
        final Clipboard clipboard =
                Toolkit.getDefaultToolkit().getSystemClipboard();
        final JButton copy = new JButton("Copy");

        final JPanel panel = new JPanel();
        panel.add(box);
        panel.add(scroll);
        panel.add(copy);
        // combo listener
        box.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textArea.setText(compiler[box.getSelectedIndex()]);
                textArea.setCaretPosition(0);
            }
        });
        // copy listener
        copy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                StringSelection data = new StringSelection(textArea.getText());
                clipboard.setContents(data, data);
                textArea.requestFocusInWindow();
                textArea.selectAll();
                JOptionPane.showConfirmDialog(null,
                        "Map successfully copied to clipboard!",
                        "",
                        JOptionPane.CLOSED_OPTION,
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        // ok listener
        JOptionPane.showConfirmDialog(null, panel, "Copy Map Code!",
                JOptionPane.CLOSED_OPTION, JOptionPane.PLAIN_MESSAGE);
    }

    public void setRows(int rows) {
        mapHandler.setRows(rows);
    }

    public void setCols(int cols) {
        mapHandler.setCols(cols);
    }

    public void setRowsAndCols(int rows, int cols, boolean clear) {
        if (!clear)
            time = mapHandler.saveMap();
        setRows(rows);
        setCols(cols);
    }

    public void help() {
        JPanel panel = new JPanel();
        JButton button1 = new JButton("");
        JButton button2 = new JButton("");
        panel.add(button1);
        panel.add(button2);
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        JOptionPane.showConfirmDialog(null, panel, "Learn More!",
                JOptionPane.CLOSED_OPTION, JOptionPane.PLAIN_MESSAGE);
    }
}