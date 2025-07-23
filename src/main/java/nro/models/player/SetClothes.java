package nro.models.player;

import nro.consts.ConstItem;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.services.Service;

/**
 *
 */
public class SetClothes {

    private Player player;

    public SetClothes(Player player) {
        this.player = player;
    }

    public byte songoku2;
    public byte thienXinHang2;
    public byte kaioken2;

    public byte lienhoan2;
    public byte pikkoroDaimao2;
    public byte picolo2;

    public byte kakarot2;
    public byte cadic2;
    public byte nappa2;

    public byte songoku1;
    public byte thienXinHang1;
    public byte kaioken1;

    public byte lienhoan1;
    public byte pikkoroDaimao1;
    public byte picolo1;

    public byte kakarot1;
    public byte cadic1;
    public byte nappa1;

    public byte SetHuyDiet;

    public byte godClothes;
    public int ctHaiTac = -1;
    public int ctBunmaXecXi = -1;

    public int setLevel7;
    public int setLevel8;

    public int set8Star;
    public int set7Star;


    public void setup() {
        setDefault();
        setupSKT();
        setUpSetHhuyDiet();

//        setUpLevelSetClothe();
        setupSetSao();

        Item ct = this.player.inventory.itemsBody.get(5);
        if (ct.isNotNullItem()) {
            switch (ct.template.id) {
                case 618:
                case 619:
                case 620:
                case 621:
                case 622:
                case 623:
                case 624:
                case 626:
                case 627:
                    this.ctHaiTac = ct.template.id;
                    break;
                case 464:
                    this.ctBunmaXecXi = ct.template.id;
                    break;
            }
        }
    }

    public static boolean isSetThanLinh(int[] array, int itemId) {
        for (int i : array) {
            if (i == itemId) {
                return true;
            }
        }
        return false;
    }

    private void setupSKT() {
        for (int i = 0; i < 5; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                boolean isActSet = false;
                for (ItemOption io : item.itemOptions) {
                    switch (io.optionTemplate.id) {
                        case 129:
                        case 141:
                            isActSet = true;
                            songoku2++;
                            break;
                        case 127:
                        case 139:
                            isActSet = true;
                            thienXinHang2++;
                            break;
                        case 128:
                        case 140:
                            isActSet = true;
                            kaioken2++;
                            break;
                        case 131:
                        case 143:
                            isActSet = true;
                            lienhoan2++;
                            break;
                        case 132:
                        case 144:
                            isActSet = true;
                            pikkoroDaimao2++;
                            break;
                        case 130:
                        case 142:
                            isActSet = true;
                            picolo2++;
                            break;
                        case 135:
                        case 138:
                            isActSet = true;
                            nappa2++;
                            break;
                        case 133:
                        case 136:
                            isActSet = true;
                            kakarot2++;
                            break;
                        case 134:
                        case 137:
                            isActSet = true;
                            cadic2++;
                            break;
                        case 210:
                        case 222:
                            isActSet = true;
                            kaioken1++;
                            break;
                        case 211:
                        case 223:
                            isActSet = true;
                            thienXinHang1++;
                            break;
                        case 216:
                        case 219:
                            isActSet = true;
                            kakarot1++;
                            break;
                        case 217:
                        case 220:
                            isActSet = true;
                            cadic1++;
                            break;
                        case 213:
                        case 225:
                            isActSet = true;
                            picolo1++;
                            break;
                        case 214:
                        case 226:
                            isActSet = true;
                            pikkoroDaimao1++;
                            break;

                    }

                    if (isActSet) {
                        break;
                    }
                }
            } else {
                break;
            }
        }
    }

    void setUpSetHhuyDiet() {
        for (int i = 0; i < 5; i++) {
            Item it = this.player.inventory.itemsBody.get(i);
            if (it != null && it.isNotNullItem()) {
                if (it.getId() == ConstItem.doSKHVip[i][this.player.gender][13]) {
                    this.SetHuyDiet++;
                } else if (it.getId() == ConstItem.doSKHVip[i][this.player.gender][12]) {
                    godClothes++;
                }
            }
        }
    }


    void setUpLevelSetClothe() {
        if (player.isPlnPet()) {
            for (int i = 0; i < 5; i++) {
                Item it = this.player.inventory.itemsBody.get(i);
                if (it != null && it.isNotNullItem()) {
                    if (it.haveOption(72)) {
                        int param = it.getOption(72).param;
                        if (param == 8) {
                            setLevel8++;
                        } else if (param >= 7) {
                            setLevel7++;
                        }
                    }
                }
            }
        }
    }

    void setupSetSao() {
        if (player.isPlnPet()) {
            for (int i = 0; i < 5; i++) {
                Item it = this.player.inventory.itemsBody.get(i);
                if (it != null && it.isNotNullItem()) {
                    if (it.haveOption(107)) {
                        int param = it.getOption(107).param;
                        if (param == 8) {
                            set8Star++;
                        } else if (param >= 7) {
                            set7Star++;
                        }

                    }
                }
            }
        }
    }

    private void setDefault() {
        this.songoku1 = 0;
        this.thienXinHang1 = 0;
        this.kaioken1 = 0;
        this.lienhoan1 = 0;
        this.pikkoroDaimao1 = 0;
        this.picolo1 = 0;
        this.kakarot1 = 0;
        this.cadic1 = 0;
        this.nappa1 = 0;
        this.songoku2 = 0;
        this.SetHuyDiet = 0;
        this.thienXinHang2 = 0;
        this.kaioken2 = 0;
        this.lienhoan2 = 0;
        this.pikkoroDaimao2 = 0;
        this.picolo2 = 0;
        this.kakarot2 = 0;
        this.cadic2 = 0;
        this.nappa2 = 0;
        this.godClothes = 0;
        this.ctHaiTac = -1;
        this.ctHaiTac = -1;
        this.ctBunmaXecXi = -1;
        this.setLevel7 = 0;
        this.setLevel8 = 0;
        this.set7Star = 0;
        this.set8Star = 0;
    }

    public void dispose() {
        this.player = null;
    }
}
