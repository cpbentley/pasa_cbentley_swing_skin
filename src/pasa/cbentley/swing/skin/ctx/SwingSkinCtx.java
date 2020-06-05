/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.swing.skin.ctx;

import java.util.List;
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

   /**
    * Will not be null
    * @return
    */
   public IPrefs getUIPref() {
      return sc.getPrefs();
   }

   public void addI18NKey(List<String> list) {
      list.add("i18nSwingSkin");
   }

   public int getCtxID() {
      return 445;
   }
}
