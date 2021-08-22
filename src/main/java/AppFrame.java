import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class AppFrame extends JFrame {

    private Font font;
    private JFileChooser chooser;
    private Container container;
    private List<JTextField> questionList;
    private List<JPasswordField> answerList;
    private JTextArea secretText;
    private JLabel info;
    private AppState appState;

    public AppFrame(String title) {
        super(title);
        this.setSize(Constants.FRAME_WIDTH, Constants.FRAME_HEIGHT);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setupFont();
        this.setupFileChooser();
        this.addMenuItemsAndMenuBar();
        this.setupContainerAndLayout();
        this.questionList = new ArrayList<>();
        this.answerList = new ArrayList<>();
        this.setupQuestionAndAnswer(Constants.QUESTION_1);
        this.setupQuestionAndAnswer(Constants.QUESTION_2);
        this.setupQuestionAndAnswer(Constants.QUESTION_3);
        this.setupSecretText();
        this.addDecryptButton();
        this.addScrollPanel();
        this.setupAppInfo();
        this.addIconIfExist();
        this.appState = new AppState();
    }

    private void setupFont() {
        this.font = new Font(Constants.FONT_FAMILY, Font.PLAIN, Constants.FONT_SIZE);
    }

    private void setupFileChooser() {
        this.chooser = new JFileChooser();
        this.chooser.setMultiSelectionEnabled(false);
        this.chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        this.chooser.setFileFilter(new FileNameExtensionFilter(Constants.FILE_DESC, Constants.FILE_EXTENSION));
    }

    private void addMenuItemsAndMenuBar() {
        JMenuItem open = new JMenuItem(new AbstractAction(Constants.MENU_OPEN_DESC) {
            @Override
            public void actionPerformed(ActionEvent e) {
                AppFrame.this.chooser.showOpenDialog(AppFrame.this);
                File file = chooser.getSelectedFile();
                AppFrame.this.appState.readFromFile(file, AppFrame.this.questionList, AppFrame.this.secretText);
            }
        });
        JMenuItem save = new JMenuItem(new AbstractAction(Constants.MENU_SAVE_DESC) {
            @Override
            public void actionPerformed(ActionEvent e) {
                AppFrame.this.info.setText(Constants.INFO_CLEAN);
                if (AppFrame.this.appState.canSave(AppFrame.this.answerList, AppFrame.this.secretText)) {
                    AppFrame.this.chooser.showSaveDialog(AppFrame.this);
                    File file = chooser.getSelectedFile();
                    if (!file.toString().toLowerCase().endsWith("." + Constants.FILE_EXTENSION)) {
                        file = new File(file.toString() + "." + Constants.FILE_EXTENSION);
                    }
                    AppFrame.this.appState.saveToFile(file, AppFrame.this.questionList, AppFrame.this.answerList, AppFrame.this.secretText);
                }
                else {
                    AppFrame.this.info.setText(String.format(Constants.INFO_FORMAT, Constants.MENU_SAVE_HINT));
                }
            }
        });
        JMenu menu = new JMenu(Constants.MENU_FILE_DESC);
        menu.add(open);
        menu.add(save);
        JMenuBar bar = new JMenuBar();
        bar.add(menu);
        this.setJMenuBar(bar);
    }

    private void setupContainerAndLayout() {
        this.container = this.getContentPane();
        this.container.setLayout(new FlowLayout());
    }

    private void setupQuestionAndAnswer(String questionTitle) {
        JTextField question = new JTextField(questionTitle, Constants.QA_WIDTH);
        JPasswordField answer = new JPasswordField(Constants.QA_WIDTH);
        question.setFont(this.font);
        answer.setFont(this.font);
        this.questionList.add(question);
        this.answerList.add(answer);
        this.container.add(question);
        this.container.add(answer);
    }

    private void setupSecretText() {
        this.secretText = new JTextArea(Constants.SECRET_TXT_ROW, Constants.SECRET_TXT_COL);
        this.secretText.setFont(this.font);
    }

    private void addDecryptButton() {
        JButton decrypt = new JButton(Constants.DECRYPT_DESC);
        decrypt.setPreferredSize(new Dimension(Constants.DECRYPT_WIDTH, Constants.DECRYPT_HEIGHT));
        decrypt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AppFrame.this.info.setText(Constants.INFO_CLEAN);
                if (AppFrame.this.appState.canDecrypt(AppFrame.this.answerList)) {
                    AppFrame.this.appState.decrypt(AppFrame.this.answerList, AppFrame.this.secretText);
                }
                else {
                    AppFrame.this.info.setText(String.format(Constants.INFO_FORMAT, Constants.DECRYPT_HINT));
                }
            }
        });
        decrypt.setFont(this.font);
        this.container.add(decrypt);
    }

    private void addScrollPanel() {
        JPanel panel = new JPanel();
        TitledBorder title = new TitledBorder(new EtchedBorder(), Constants.SCROLL_TITLE);
        JScrollPane scroll = new JScrollPane(this.secretText);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        title.setTitleFont(this.font);
        panel.setBorder(title);
        panel.add(scroll);
        this.container.add(panel);
    }

    private void setupAppInfo() {
        this.info = new JLabel(Constants.INFO_CLEAN);
        this.info.setFont(this.font);
        this.info.setForeground(Color.RED);
        this.container.add(this.info);
    }

    private void addIconIfExist() {
        ImageIcon icon = new ImageIcon(Constants.ICON_FILENAME);
        this.setIconImage(icon.getImage());
    }

}
