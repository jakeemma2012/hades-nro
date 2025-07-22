package nro.services;

import nro.consts.ConstPlayer;
import nro.models.player.Pet;
import nro.models.player.Player;
import nro.utils.SkillUtil;
import nro.utils.Util;
import nro.consts.ConstPet;

/**
 *
 *
 *
 */
public class PetService {

    private static PetService i;

    public static PetService gI() {
        if (i == null) {
            i = new PetService();
        }
        return i;
    }

    public void createNormalPet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, ConstPet.NORMAL, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                    player.pet.nPoint.initPowerLimit();
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Xin hãy thu nhận làm đệ tử");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createNormalPet(Player player, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, ConstPet.NORMAL);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Xin hãy thu nhận làm đệ tử");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createMabuPet(Player player, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, ConstPet.MABU);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                    player.pet.nPoint.initPowerLimit();
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Oa oa oa...");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createMabuPet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, ConstPet.MABU, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                    player.pet.nPoint.initPowerLimit();
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Oa oa oa...");
            } catch (Exception e) {
            }
        }).start();
    }

    public void createBuGayPet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, ConstPet.BU_GAY, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Oa...Oa...OA (nhưng gầy hơn)");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void createKidBuPet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, ConstPet.KIDBU, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Yoo ông già !!!...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void createVidelPet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, ConstPet.VIDEL, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Hút cạn máu nàoooooooooooooo!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void createWhisPet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, ConstPet.WHIS, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Djt nhau au au...");
                Service.getInstance().sendThongBao(player, "Djt nhau au au");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void changeNormalPet(Player player, int gender) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        MapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createNormalPet(player, gender, limitPower);
    }

    public void changeMabuPet(Player player, int gender) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        MapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createMabuPet(player, gender, limitPower);
    }

    public void changeBuGayPet(Player player, int gender) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        MapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createBuGayPet(player, gender, limitPower);
    }

    public void changeVidelPet(Player player, int gender) {
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        MapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createVidelPet(player, gender);
    }

    public void changeKidBuPet(Player player, int gender) {
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        MapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createKidBuPet(player, gender);
    }

    public void changeNamePet(Player player, String name) {
        if (!InventoryService.gI().existItemBag(player, 400)) {
            Service.getInstance().sendThongBao(player, "Bạn cần thẻ đặt tên đệ tử, mua tại Santa");
            return;
        } else if (Util.haveSpecialCharacter(name)) {
            Service.getInstance().sendThongBao(player, "Tên không được chứa ký tự đặc biệt");
            return;
        } else if (name.length() > 10) {
            Service.getInstance().sendThongBao(player, "Tên quá dài");
            return;
        }
        MapService.gI().exitMap(player.pet);
        player.pet.name = "$" + name.toLowerCase().trim();
        InventoryService.gI().subQuantityItemsBag(player, InventoryService.gI().findItemBagByTemp(player, 400), 1);
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                Service.getInstance().chatJustForMe(player, player.pet, "Cảm ơn sư phụ đã đặt cho con tên " + name);
            } catch (Exception e) {
            }
        }).start();
    }

    public int[] getDataPetNormal() {
        int[] petData = new int[5];
        petData[0] = Util.nextInt(40, 105) * 20; //hp
        petData[1] = Util.nextInt(40, 105) * 20; //mp
        petData[2] = Util.nextInt(20, 45); //dame
        petData[3] = Util.nextInt(9, 50); //def
        petData[4] = Util.nextInt(0, 2); //crit
        return petData;
    }

    private int[] getDataPetMabu() {
        int[] petData = new int[5];
        petData[0] = Util.nextInt(40, 105) * 20; //hp
        petData[1] = Util.nextInt(40, 105) * 20; //mp
        petData[2] = Util.nextInt(50, 120); //dame
        petData[3] = Util.nextInt(9, 50); //def
        petData[4] = Util.nextInt(0, 2); //crit
        return petData;
    }

    private int[] getDataPetBerus() {
        int[] petData = new int[5];
        petData[0] = Util.nextInt(40, 115) * 20; //hp
        petData[1] = Util.nextInt(40, 115) * 20; //mp
        petData[2] = Util.nextInt(70, 140); //dame
        petData[3] = Util.nextInt(9, 50); //def
        petData[4] = Util.nextInt(0, 2); //crit
        return petData;
    }

    private int[] getDataPetVidel() {
        int[] petData = new int[5];
        petData[0] = Util.nextInt(40, 120) * 20; //hp
        petData[1] = Util.nextInt(40, 120) * 20; //mp
        petData[2] = Util.nextInt(20, 150); //dame
        petData[3] = Util.nextInt(9, 50); //def
        petData[4] = Util.nextInt(0, 2); //crit
        return petData;
    }

    private void createNewPet(Player player, byte typePet, byte... gender) {
        int[] data = new int[0];
        int petLevel = 0;
        Pet pet = new Pet(player);
        pet.nPoint.power = 1500000;

        switch (typePet) {
            case ConstPet.NORMAL: {
                data = getDataPetNormal();
                pet.name = "$Đệ tử";
                pet.nPoint.power = 2000;
                pet.typePet = ConstPet.NORMAL;
                break;
            }
            case ConstPet.MABU: {
                data = getDataPetMabu();
                pet.name = "$Mabư";
                pet.typePet = ConstPet.MABU;
                break;
            }
            case ConstPet.BU_GAY: {        // bư gầy
                data = getDataPetBerus();
                pet.name = "$Mabư ốm";
                pet.typePet = ConstPet.BU_GAY;
                break;
            }
            case ConstPet.KIDBU: {      // bư kid
                data = getDataPetBerus();
                pet.typePet = ConstPet.KIDBU;
                pet.name = "$Kid";
                break;
            }
            case ConstPet.VIDEL: {     // zamasu
                data = getDataPetVidel();
                pet.typePet = ConstPet.VIDEL;
                petLevel = Util.nextInt(10, 17);
                pet.name = "$Zamas Future [" + petLevel + "]";
                break;
            }
            case ConstPet.WHIS: {
                data = getDataPetVidel();
                pet.name = "$Whiser";
                pet.typePet = ConstPet.WHIS;
                petLevel = player.pet.getLever();
                break;
            }
        }

        pet.gender = (gender != null && gender.length != 0) ? gender[0] : (byte) Util.nextInt(0, 2);
        pet.id = -player.id;
        pet.nPoint.stamina = 1000;
        pet.nPoint.maxStamina = 1000;
        pet.nPoint.hpg = data[0];
        pet.nPoint.mpg = data[1];
        pet.nPoint.dameg = data[2];
        pet.nPoint.defg = data[3];
        pet.nPoint.critg = data[4];
        for (int i = 0; i < 7; i++) {
            pet.inventory.itemsBody.add(ItemService.gI().createItemNull());
        }
        pet.playerSkill.skills.add(SkillUtil.createSkill(0, 1));
//        if (petLevel != 0) {
//            pet.playerSkill.skills.add(SkillUtil.createSkill(1, 1));
//        }
        for (int i = 0; i < 3; i++) {
            pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
        }
        pet.nPoint.calPoint();
        pet.nPoint.setFullHpMp();

        if (petLevel != 0) {
            pet.setLever(petLevel);
        }

        player.pet = pet;
    }

    //--------------------------------------------------------------------------
}
