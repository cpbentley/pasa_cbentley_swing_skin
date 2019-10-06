package pasa.cbentley.swing.skin.ctx;

import java.util.prefs.Preferences;

import pasa.cbentley.core.src4.interfaces.ITechPrefs;

public interface ITechPrefsSwingSkin extends ITechPrefs {
   /**
    * Key String for {@link Preferences}
    */
   public static final String   PREF_LOOKANDFEEL       = "LookAndFeel";

   /**
    * Key for listing Look/Theme favorite
    * <br>
    * Its a big String with 
    */
   public static final String   PREF_LOOKANDFEEL_FAV   = "LookAndFeelFavs";

   public static final String   PREF_LOOKANDFEEL_FLAG  = "LookAndFeelFlag";

   /**
    * Theme Key String for {@link Preferences}
    */
   public static final String   PREF_LOOKANDFEEL_THEME = "LookAndFeelTheme";
}
