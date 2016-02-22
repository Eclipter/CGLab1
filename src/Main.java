import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * Created by USER on 19.02.2016.
 */
public class Main {
    private JSlider sliderR;
    private JSlider sliderG;
    private JSlider sliderB;
    private JSlider sliderLu;
    private JSlider sliderU;
    private JSlider sliderV;
    private JSlider sliderH;
    private JSlider sliderL;
    private JSlider sliderS;
    private JSlider sliderC;
    private JSlider sliderM;
    private JSlider sliderY;
    private JPanel colorPanel;
    private JPanel controlPanel;
    private JPanel mainPanel;
    private JPanel LUVPanel;
    private JPanel RGBPanel;
    private JPanel CMYPanel;
    private JPanel HLSPanel;
    private JTextField textFieldR;
    private JTextField textFieldG;
    private JTextField textFieldB;
    private JTextField textFieldLu;
    private JTextField textFieldU;
    private JTextField textFieldV;
    private JTextField textFieldH;
    private JTextField textFieldL;
    private JTextField textFieldS;
    private JTextField textFieldC;
    private JTextField textFieldM;
    private JTextField textFieldY;
    private JPanel errorPanel;
    private JLabel errorLabel;
    private Controller controller;
    private boolean isUserChange;

    public void changeBackgroundColor() {
        colorPanel.setBackground(new Color((int) controller.getrValue(),
                (int) controller.getgValue(), (int) controller.getbValue()));
    }

    public void changeSlidersForRGB() {
        sliderR.setValue((int) (controller.getrValue()));
        sliderG.setValue((int) (controller.getgValue()));
        sliderB.setValue((int) (controller.getbValue()));
    }

    public void changeSlidersForHLS() {
        sliderH.setValue((int) (controller.gethValue()));
        sliderL.setValue((int) (controller.getlValue() * 100));
        sliderS.setValue((int) (controller.getsValue() * 100));
    }

    public void changeSlidersForCMY() {
        sliderC.setValue((int) (controller.getcValue() * 100));
        sliderM.setValue((int) (controller.getmValue() * 100));
        sliderY.setValue((int) (controller.getyValue() * 100));
    }

    public void changeSlidersForLUV() {
        sliderLu.setValue((int) (controller.getLuValue()));
        sliderU.setValue((int) (controller.getuValue() * 100 / 0.6));
        sliderV.setValue((int) (controller.getvValue() * 100 / 0.6));
    }

    public void changeTextForAll() {
        textFieldR.setText(String.valueOf((int) (controller.getrValue())));
        textFieldG.setText(String.valueOf((int) (controller.getgValue())));
        textFieldB.setText(String.valueOf((int) (controller.getbValue())));
        textFieldH.setText(String.valueOf((int) (controller.gethValue())));
        textFieldL.setText(String.valueOf((int) (controller.getlValue() * 100)));
        textFieldS.setText(String.valueOf((int) (controller.getsValue() * 100)));
        textFieldC.setText(String.valueOf((int) (controller.getcValue() * 100)));
        textFieldM.setText(String.valueOf((int) (controller.getmValue() * 100)));
        textFieldY.setText(String.valueOf((int) (controller.getyValue() * 100)));
        textFieldLu.setText(String.valueOf((int) (controller.getLuValue())));
        textFieldU.setText(String.valueOf((int) (controller.getuValue() * 100 / 0.6)));
        textFieldV.setText(String.valueOf((int) (controller.getvValue() * 100 / 0.6)));
    }

    public void recountColorsFromRGB() {
        errorLabel.setText("");
        controller.fromRGBtoHLS();
        controller.fromRGBtoCMY();
        controller.fromXYZtoLUV(controller.fromRGBtoXYZ());

        isUserChange = true;
        changeSlidersForCMY();
        changeSlidersForHLS();
        changeSlidersForLUV();
        changeTextForAll();
        isUserChange = false;
    }

    public void recountColorsFromCMY() {
        errorLabel.setText("");
        controller.fromCMYtoRGB();
        controller.fromRGBtoHLS();
        controller.fromXYZtoLUV(controller.fromRGBtoXYZ());

        isUserChange = true;
        changeSlidersForRGB();
        changeSlidersForHLS();
        changeSlidersForLUV();
        changeTextForAll();
        isUserChange = false;
    }

    public void recountColorsFromHLS() {
        errorLabel.setText("");
        controller.fromHLStoRGB();
        controller.fromRGBtoCMY();
        controller.fromXYZtoLUV(controller.fromRGBtoXYZ());

        isUserChange = true;
        changeSlidersForRGB();
        changeSlidersForCMY();
        changeSlidersForLUV();
        changeTextForAll();
        isUserChange = false;
    }

    public void recountColorsFromLUV() {
        errorLabel.setText("");
        boolean colorsPure = controller.fromXYZtoRGB(controller.fromLUVtoXYZ());
        controller.fromRGBtoCMY();
        controller.fromRGBtoHLS();

        if (!colorsPure) {
            errorLabel.setText("Not supported by RGB");
        }

        isUserChange = true;
        changeSlidersForRGB();
        changeSlidersForHLS();
        changeSlidersForCMY();
        changeTextForAll();
        isUserChange = false;
    }

    public Main() {
        isUserChange = false;
        this.controller = new Controller();
        controller.init();
        recountColorsFromRGB();

        sliderR.addChangeListener(e -> {
            if (isUserChange) {
                return;
            }
            JSlider source = (JSlider) e.getSource();
            int value = source.getValue();
            controller.setrValue(value);
            recountColorsFromRGB();
            changeBackgroundColor();

        });
        sliderG.addChangeListener(e -> {
            if (isUserChange) {
                return;
            }
            JSlider source = (JSlider) e.getSource();
            int value = source.getValue();
            controller.setgValue(value);
            recountColorsFromRGB();
            changeBackgroundColor();
        });
        sliderB.addChangeListener(e -> {
            if (isUserChange) {
                return;
            }
            JSlider source = (JSlider) e.getSource();
            int value = source.getValue();
            controller.setbValue(value);
            recountColorsFromRGB();
            changeBackgroundColor();
        });
        sliderH.addChangeListener(e -> {
            if (isUserChange) {
                return;
            }
            JSlider source = (JSlider) e.getSource();
            int value = source.getValue();
            controller.sethValue((double) value);
            recountColorsFromHLS();
            changeBackgroundColor();
        });
        sliderL.addChangeListener(e -> {
            if (isUserChange) {
                return;
            }
            JSlider source = (JSlider) e.getSource();
            int value = source.getValue();
            controller.setlValue((double) value / 100);
            recountColorsFromHLS();
            changeBackgroundColor();
        });
        sliderS.addChangeListener(e -> {
            if (isUserChange) {
                return;
            }
            JSlider source = (JSlider) e.getSource();
            int value = source.getValue();
            controller.setsValue((double) value / 100);
            recountColorsFromHLS();
            changeBackgroundColor();
        });
        sliderC.addChangeListener(e -> {
            if (isUserChange) {
                return;
            }
            JSlider source = (JSlider) e.getSource();
            int value = source.getValue();
            controller.setcValue((double) value / 100);
            recountColorsFromCMY();
            changeBackgroundColor();
        });
        sliderM.addChangeListener(e -> {
            if (isUserChange) {
                return;
            }
            JSlider source = (JSlider) e.getSource();
            int value = source.getValue();
            controller.setmValue((double) value / 100);
            recountColorsFromCMY();
            changeBackgroundColor();
        });
        sliderY.addChangeListener(e -> {
            if (isUserChange) {
                return;
            }
            JSlider source = (JSlider) e.getSource();
            int value = source.getValue();
            controller.setyValue((double) value / 100);
            recountColorsFromCMY();
            changeBackgroundColor();
        });
        sliderLu.addChangeListener(e -> {
            if (isUserChange) {
                return;
            }
            JSlider source = (JSlider) e.getSource();
            int value = source.getValue();
            controller.setLuValue((double) value);
            recountColorsFromLUV();
            changeBackgroundColor();
        });
        sliderU.addChangeListener(e -> {
            if (isUserChange) {
                return;
            }
            JSlider source = (JSlider) e.getSource();
            int value = source.getValue();
            controller.setuValue((double) value / 100 * 0.6);
            recountColorsFromLUV();
            changeBackgroundColor();
        });
        sliderV.addChangeListener(e -> {
            if (isUserChange) {
                return;
            }
            JSlider source = (JSlider) e.getSource();
            int value = source.getValue();
            controller.setvValue((double) value / 100 * 0.6);
            recountColorsFromLUV();
            changeBackgroundColor();
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Ultimate Color Panel");
        /*try {
            UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        frame.setContentPane(new Main().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.pack();
        frame.setVisible(true);
    }
}
