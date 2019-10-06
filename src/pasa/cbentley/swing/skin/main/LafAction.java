package pasa.cbentley.swing.skin.main;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.UIManager;

import com.jtattoo.plaf.AbstractLookAndFeel;

import pasa.cbentley.swing.skin.ctx.SwingSkinCtx;

/**
    * Each Theme menu items will be represented by a {@link LafAction}.
    * @author Charles Bentley
    *
    */
public class LafAction extends AbstractAction {

   /**
    * 
    */
   private static final long         serialVersionUID = -2891206240367644231L;

   private AbstractLookAndFeel       alf;

   private UIManager.LookAndFeelInfo laf;

   private JMenu                     menu;

   private SwingSkinCtx              ssc;

   private SwingSkinManager          ssm;

   private String                    theme;

   /**
    * 
    * @param ssc TODO
    * @param ssm TODO
    * @param laf
    * @param menu the {@link JMenu} that will hold this action
    * @throws NullPointerException if laf is null
    */
   public LafAction(SwingSkinCtx ssc, SwingSkinManager ssm, UIManager.LookAndFeelInfo laf, JMenu menu) {
      super(laf.getName());
      this.ssc = ssc;
      this.ssm = ssm;
      this.laf = laf;
      this.menu = menu;
   }

   /**
    * 
    * @param laf
    * @param theme theme String
    * @param alf the {@link AbstractLookAndFeel}
    * @param menu the {@link JMenu} that will hold this action
    * @throws NullPointerException if laf is null
    */
   public LafAction(SwingSkinCtx ssc, SwingSkinManager ssm, UIManager.LookAndFeelInfo laf, String theme, AbstractLookAndFeel alf, JMenu menu) {
      super(laf.getName() + (theme != null ? (" " + theme) : ""));
      this.ssc = ssc;
      this.ssm = ssm;
      this.laf = laf;
      this.theme = theme;
      this.alf = alf;
      this.menu = menu;
   }

   /**
    * 
    */
   public void actionPerformed(ActionEvent e) {
      //System.out.println("LafAction#actionPerformed " + theme);
      ssm.executeSetMyLafTheme(this);
   }

   public AbstractLookAndFeel getAbstractLookAndFeel() {
      return alf;
   }

   public JMenu getActionRootMenu() {
      return getMenu();
   }

   public UIManager.LookAndFeelInfo getInfo() {
      return laf;
   }

   public JMenu getMenu() {
      return menu;
   }

   public String getTheme() {
      return theme;
   }

   public boolean isLf(String lf) {
      return laf.getClassName().equals(lf);
   }

   public boolean isMatch(LafAction action) {
      return isMatch(action.laf.getName(), action.theme);
   }

   public boolean isMatch(String lafName, String theme2) {
      if (lafName.equals(laf.getName())) {
         if (theme == null && theme2 == null) {
            return true;
         } else if (theme != null && theme2 != null) {
            return theme.equals(theme2);
         }
      }
      return false;
   }

}