package pasa.cbentley.swing.skin.ctx;

import java.util.prefs.Preferences;

import pasa.cbentley.core.src4.ctx.ACtx;
import pasa.cbentley.core.src4.interfaces.IPrefs;
import pasa.cbentley.swing.ctx.SwingCtx;

public class SwingSkinCtx extends ACtx {

   protected final SwingCtx sc;

   public SwingSkinCtx(SwingCtx sc) {
      super(sc.getUCtx());
      this.sc = sc;
   }

   public SwingCtx getSwingCtx() {
      return sc;
   }

   public IPrefs getUIPref() {
      return sc.getPrefs();
   }

}
