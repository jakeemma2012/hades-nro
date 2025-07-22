package nro.models.npc;

public class ImageMenu {

    public String menu;
    public int iconID;
    public int X;
    public int Y;

    public ImageMenu() {
    }

    public ImageMenu(String text, int icon) {
        this.menu = text;
        if (icon > 0) {
            this.iconID = icon;
        } else {
            this.iconID = 0;
        }
        this.X = 0;
        this.Y = 0;
    }

    public ImageMenu(String text) {
        this.menu = text;
        this.iconID = 0;
        this.X = 0;
        this.Y = 0;
    }
    
     public ImageMenu(int icon) {
        this.menu = "";
        this.iconID = icon;
        this.X = 15;
        this.Y = 15;
    }
    

    public ImageMenu(String text, int icon, int X, int Y) {
        this.menu = text;
        this.iconID = icon;
        this.X = X;
        this.Y = Y;
    }
    
    
}
