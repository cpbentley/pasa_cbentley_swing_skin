package pasa.cbentley.swing.skin.main;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import pasa.cbentley.swing.skin.ctx.SwingSkinCtx;

public class MouseSkinListener implements MouseListener, MouseWheelListener {

   protected final SwingSkinCtx ssc;
   protected final SwingSkinManager ssm;

   public MouseSkinListener(SwingSkinCtx ssc, SwingSkinManager ssm) {
      this.ssc = ssc;
      this.ssm = ssm;
      
   }
   public void mouseWheelMoved(MouseWheelEvent e) {
      
   }

   public void mouseClicked(MouseEvent e) {
      
   }

   public void mousePressed(MouseEvent e) {
      
   }

   public void mouseReleased(MouseEvent e) {
      
   }

   public void mouseEntered(MouseEvent e) {
     ssm.checkPopulateRootMenu();
   }

   public void mouseExited(MouseEvent e) {
      
   }

}
