package pasa.cbentley.swing.skin.run;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import pasa.cbentley.core.src4.interfaces.IPrefs;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.swing.run.RunSwingAbstract;
import pasa.cbentley.swing.skin.ctx.SwingSkinCtx;
import pasa.cbentley.swing.skin.main.SwingSkinManager;
import pasa.cbentley.swing.window.CBentleyFrame;

public class RunSwingSkinManagerDef extends RunSwingAbstract {

   /**
    * Simple test method
    * @param args
    */
   public static void main(String[] args) {
      RunSwingSkinManagerDef runner = new RunSwingSkinManagerDef();
      runner.initUIThreadOutside();
      javax.swing.SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            runner.initUIThreadInside();
         }
      });
   }

   protected final SwingSkinCtx ssc;

   private SwingSkinManager     lfm;

   public RunSwingSkinManagerDef() {
      ssc = new SwingSkinCtx(sc);
   }

   protected void addI18n(List<String> list) {
      list.add("i18nSwingSkin");
   }

   public void cmdExit() {
      lfm.prefsSave();
   }

   protected void initOutsideUIForPrefs(IPrefs prefs) {

   }

   protected CBentleyFrame initUIThreadInsideSwing() {
      try {
         // Set System L&F
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } catch (UnsupportedLookAndFeelException e) {
         // handle exception
      } catch (ClassNotFoundException e) {
         // handle exception
      } catch (InstantiationException e) {
         // handle exception
      } catch (IllegalAccessException e) {
         // handle exception
      }
      lfm = new SwingSkinManager(ssc);
      lfm.setIconSelected(new Icon() {
         public int getIconHeight() {
            return 16;
         }

         public int getIconWidth() {
            return 16;
         }

         public void paintIcon(Component c, Graphics g, int x, int y) {
            g.setColor(Color.blue);
            g.fillRect(x, y, 16, 16);
         }
      });
      final CBentleyFrame frame = new CBentleyFrame(sc, "lookfeelframe");

      JMenuBar mb = new JMenuBar();
      mb.add(lfm.getRootMenu());
      JMenu jm = new JMenu("Options");
      final JRadioButtonMenuItem jiUndecorated = new JRadioButtonMenuItem("Undecorated");
      jiUndecorated.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            boolean isDecorated = !frame.isUndecorated();
            boolean isDecoSupport = JFrame.isDefaultLookAndFeelDecorated();

            System.out.println("isDecorated=" + isDecorated + " isDecoSupport=" + isDecoSupport);

            frame.dispose();
            frame.setUndecorated(true);
            frame.setVisible(true);
         }
      });
      final JRadioButtonMenuItem jiDecorated = new JRadioButtonMenuItem("Decorated");
      jiDecorated.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            boolean isUndecorated = frame.isUndecorated();
            if (isUndecorated) {
               frame.dispose();
               frame.setUndecorated(false);
               frame.setVisible(true);
            }
         }
      });
      ButtonGroup group = new ButtonGroup();
      group.add(jiDecorated);
      group.add(jiUndecorated);
      jm.add(jiDecorated);
      jm.add(jiUndecorated);

      mb.add(jm);
      frame.setJMenuBar(mb);

      JButton but = new JButton("Hello World!");
      frame.getContentPane().add(but);
      frame.pack();
      frame.setSize(300, 200);
      frame.setLocation(400, 400);
      frame.setVisible(true);

      if (frame.isUndecorated()) {
         jiUndecorated.setSelected(true);
      } else {
         jiDecorated.setSelected(true);
      }

      return frame;
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, "RunSwingSkinManager");
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "RunSwingSkinManager");
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   private void toStringPrivate(Dctx dc) {

   }

   //#enddebug

}
