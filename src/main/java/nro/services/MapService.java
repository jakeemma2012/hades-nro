package nro.services;

import nro.models.map.Map;
import nro.models.map.WayPoint;
import nro.models.map.Zone;
import nro.models.map.war.BlackBallWar;
import nro.models.player.NPoint;
import nro.models.player.Player;
import nro.server.Manager;
import nro.server.io.Message;
import nro.utils.Log;
import nro.utils.Util;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import nro.models.boss.list_boss.WhisTop;

/**
 * 
 */
public class MapService {

    private static MapService i;

    public static MapService gI() {
        if (i == null) {
            i = new MapService();
        }
        return i;
    }

    public WayPoint getWaypointPlayerIn(Player player) {
        for (WayPoint wp : player.zone.map.wayPoints) {
            if (player.location.x >= wp.minX && player.location.x <= wp.maxX && player.location.y >= wp.minY
                    && player.location.y <= wp.maxY) {
                return wp;
            }
        }
        return null;
    }

    /**
     * @param tileTypeFocus tile type: top, bot, left, right...
     * @return [tileMapId][tileType]
     */
    public int[][] readTileIndexTileType(int tileTypeFocus) {
        int[][] tileIndexTileType = null;
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream("AbsReSource/tile_set_info"));
            int numTileMap = dis.readByte();
            tileIndexTileType = new int[numTileMap][];
            for (int i = 0; i < numTileMap; i++) {
                int numTileOfMap = dis.readByte();
                for (int j = 0; j < numTileOfMap; j++) {
                    int tileType = dis.readInt();
                    int numIndex = dis.readByte();
                    if (tileType == tileTypeFocus) {
                        tileIndexTileType[i] = new int[numIndex];
                    }
                    for (int k = 0; k < numIndex; k++) {
                        int typeIndex = dis.readByte();
                        if (tileType == tileTypeFocus) {
                            tileIndexTileType[i][k] = typeIndex;
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.error(MapService.class, e);
        }
        return tileIndexTileType;
    }

    // tilemap for paint
    public int[][] readTileMap(int mapId) {
        int[][] tileMap = null;
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream("AbsReSource/Tile/" + mapId));
            int w = dis.readByte();
            int h = dis.readByte();
            tileMap = new int[h][w];
            for (int i = 0; i < tileMap.length; i++) {
                for (int j = 0; j < tileMap[i].length; j++) {
                    tileMap[i][j] = dis.readByte();
                }
            }
            dis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tileMap;
    }

    public Zone getMapCanJoin(Player player, int mapId) {
        if (isMapDoanhTrai(mapId)) {
            return DoanhTraiService.gI().getMapDoanhTrai(player, mapId);
        } else if (isMapBanDoKhoBau(mapId)) {
            return BanDoKhoBauService.gI().getMapBanDoKhoBau(player, mapId);
        } else if (isMapOffline(mapId)) {
            return getZoneJoinByMapIdAndZoneId(player, mapId, 0);
        }
        Zone mapJoin = null;
        Map map = getMapById(mapId);
        for (Zone zone : map.zones) {
            if (zone.getNumOfPlayers() < Zone.PLAYERS_TIEU_CHUAN_TRONG_MAP) {
                mapJoin = zone;
                break;
            }
        }
        // init new zone
        return mapJoin;
    }

    public Zone getMapCanJoin(Player player, int mapId, int zoneId) {
        Zone mapJoin = null;
        Map map = getMapById(mapId);
        for (Zone zone : map.zones) {
            if (zone.getNumOfPlayers() < Zone.PLAYERS_TIEU_CHUAN_TRONG_MAP) {
                mapJoin = zone;
                break;
            }
        }
        // init new zone
        return mapJoin;
    }

    // public Map getMapById(int mapId) {
    // return Manager.MAPS.stream().filter(map -> map.mapId ==
    // mapId).findAny().orElse(null);
    // }
    public Map getMapById(int mapId) {
        if (Manager.MAPS != null && !Manager.MAPS.isEmpty()) {
            int count = Manager.MAPS.size();
            for (int j = 0; j < count; j++) {
                Map item = Manager.MAPS.get(j);
                if (item.mapId == mapId) {
                    return item;
                }
            }
        }
        return null;
    }

    public Zone getMapCanJoinTask(Player player, int mapId) {
        Zone mapJoin = null;
        Map map = getMapById(mapId);
        for (Zone zone : map.zones) {
            if (zone.getNumOfPlayers() < Zone.PLAYERS_TIEU_CHUAN_TRONG_MAP
                    && !TaskService.gI().haveTDSTInZone(zone.getBosses())) {
                mapJoin = zone;
                break;
            }
        }
        // init new zone
        return mapJoin;
    }

    public Map getMapForCalich() {
        int mapId = Util.nextInt(27, 29);
        return MapService.gI().getMapById(mapId);
    }

    public Zone getZoneJoinByMapIdAndZoneId(Player player, int mapId, int zoneId) {
        Map map = getMapById(mapId);
        Zone zoneJoin = null;
        try {
            if (map != null && zoneId >= 0 && zoneId < map.zones.size()) {// debug
                zoneJoin = map.zones.get(zoneId);
            }
        } catch (Exception e) {
            Log.error(MapService.class, e);
        }
        return zoneJoin;
    }

    /**
     * Trả về 1 map random cho boss
     *
     * @param mapId
     * @return
     */
    public Zone getMapWithRandZone(int mapId) {
        Map map = MapService.gI().getMapById(mapId);
        Zone zone = null;
        try {
            if (map != null) {
                zone = map.zones.get(Util.nextInt(0, map.zones.size() - 1));
            }
        } catch (Exception e) {
        }
        return zone;
    }

    public String getPlanetName(byte planetId) {
        switch (planetId) {
            case 0:
                return "Trái đất";
            case 1:
                return "Namếc";
            case 2:
                return "Xayda";
            default:
                return "";
        }
    }

    /**
     * lấy danh sách map cho capsule
     *
     * @param pl
     * @return
     */
    public List<Zone> getMapCapsule(Player pl) {
        List<Zone> list = new ArrayList<>();
        if (pl.mapBeforeCapsule != null
                && pl.mapBeforeCapsule.map.mapId != 21
                && pl.mapBeforeCapsule.map.mapId != 22
                && pl.mapBeforeCapsule.map.mapId != 23
                && !isMapTuongLai(pl.mapBeforeCapsule.map.mapId)) {
            addListMapCapsule(pl, list, pl.mapBeforeCapsule);
        }
        addListMapCapsule(pl, list, getZoneJoinByMapIdAndZoneId(pl, 21 + pl.gender, 0));
        addListMapCapsule(pl, list, getZoneJoinByMapIdAndZoneId(pl, 47, 0));
        addListMapCapsule(pl, list, getZoneJoinByMapIdAndZoneId(pl, 45, 0));
        addListMapCapsule(pl, list, getZoneJoinByMapIdAndZoneId(pl, 0, 0));
        addListMapCapsule(pl, list, getZoneJoinByMapIdAndZoneId(pl, 7, 0));
        addListMapCapsule(pl, list, getZoneJoinByMapIdAndZoneId(pl, 14, 0));
        addListMapCapsule(pl, list, getZoneJoinByMapIdAndZoneId(pl, 5, 0));
        addListMapCapsule(pl, list, getZoneJoinByMapIdAndZoneId(pl, 20, 0));
        addListMapCapsule(pl, list, getZoneJoinByMapIdAndZoneId(pl, 13, 0));
        addListMapCapsule(pl, list, getZoneJoinByMapIdAndZoneId(pl, 24 + pl.gender, 0));
        addListMapCapsule(pl, list, getZoneJoinByMapIdAndZoneId(pl, 27, 0));
        addListMapCapsule(pl, list, getZoneJoinByMapIdAndZoneId(pl, 19, 0));
        addListMapCapsule(pl, list, getZoneJoinByMapIdAndZoneId(pl, 79, 0));
        addListMapCapsule(pl, list, getZoneJoinByMapIdAndZoneId(pl, 84, 0));
        return list;
    }

    public List<Zone> getMapBlackBall() {
        List<Zone> list = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            list.add(getMapById(85 + i).zones.get(0));
        }
        return list;
    }

    private void addListMapCapsule(Player pl, List<Zone> list, Zone zone) {
        for (Zone z : list) {
            if (z != null && zone != null && z.map.mapId == zone.map.mapId) {
                return;
            }
        }
        if (zone != null && pl.zone.map.mapId != zone.map.mapId) {
            list.add(zone);
        }
    }

    public void sendPlayerMove(Player player) {
        Message msg;
        try {
            msg = new Message(-7);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeShort(player.location.x);
            msg.writer().writeShort(player.location.y);
            Service.getInstance().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(MapService.class, e);
        }
    }

    public void sendPlayerMovePlayer(Player player) {
        Message msg;
        try {
            msg = new Message(-7);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeShort(player.location.x);
            msg.writer().writeShort(player.location.y);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(MapService.class, e);
        }
    }

    public void goToMap(Player player, Zone zoneJoin) {
        Zone oldZone = player.zone;
        if (oldZone != null) {
            exitMap(player);
            if (player.mobMe != null) {
                player.mobMe.goToMap(zoneJoin);
            }
        }
        player.zone = zoneJoin;
        player.zone.addPlayer(player);
    }

    public void exitMap(Player player) {
        if (player.zone != null) {
            BlackBallWar.gI().dropBlackBall(player);
            if (player.effectSkill.useTroi) {
                EffectSkillService.gI().removeUseTroi(player);
            }
            NPoint n = player.nPoint;
            if (n != null) {
                n.buffDefenseSatellite = false;
                n.buffExpSatellite = false;
            }
            player.zone.removePlayer(player);
            if (!player.zone.map.isMapOffline) {
                Message msg;
                try {
                    msg = new Message(-6);
                    msg.writer().writeInt((int) player.id);
                    Service.getInstance().sendMessAnotherNotMeInMap(player, msg);
                    msg.cleanup();
                    player.zone = null;
                } catch (Exception e) {
                    Log.error(MapService.class, e);
                }
            } else {
                if (player instanceof WhisTop) {
                    Message msg;
                    try {
                        msg = new Message(-6);
                        msg.writer().writeInt((int) player.id);
                        Service.getInstance().sendMessToPlayer(player, msg,
                                (Long) Util.GetPropertyByName(player, "player_id"));
                        msg.cleanup();
                        player.zone = null;
                    } catch (Exception e) {
                        Log.error(MapService.class, e);
                    }
                }
            }
        }
    }

    public Zone getZoneJoinByMapIdAndZoneIdTask(Player player, int mapId, int zoneId) {
        Map map = getMapById(mapId);
        Zone zoneJoin = null;
        try {
            if (map != null && zoneId >= 0 && zoneId < map.zones.size()) {// debug
                zoneJoin = map.zones.get(zoneId);

                if (TaskService.gI().haveTDSTInZone(zoneJoin.getBosses())) {
                    zoneJoin = null;
                }
            }
        } catch (Exception e) {
            Log.error(MapService.class, e);
        }
        return zoneJoin;
    }

    public boolean isMapOffline(int mapId) {
        for (Map map : Manager.MAPS) {
            if (map.mapId == mapId) {
                return map.isMapOffline;
            }
        }
        return false;
    }

    public boolean isMapBlackBallWar(int mapId) {
        return mapId >= 85 && mapId <= 91;
    }

    public boolean isMapNgucTu(int mapId) {
        return mapId == 155;
    }

    public boolean isMapMabuWar14H(int mapId) {
        return mapId == 114 || mapId == 128;
    }

    public boolean isMapMabuWar(int mapId) {
        return mapId >= 114 && mapId <= 120;
    }

    public boolean isMapVS(int mapId) {
        return mapId == 129;
    }

    public boolean isMapCDRD(int mapId) {
        return mapId >= 141 && mapId <= 144;
    }

    public boolean isMapFuture(int mapId) {
        return mapId >= 92 && mapId <= 103;
    }

    public boolean isMapFide(int mapId) {
        return mapId >= 63 && mapId <= 83;
    }

    public boolean isMapBang(int mapId) {
        return mapId >= 156 && mapId <= 159;
    }

    public boolean isMapHanhTinhXanh(int mapId) {
        return mapId == 190;
    }

    public boolean isMapDiaNguc(Map map) {
        int mapId = map.mapId;
        return mapId >= 206 && mapId <= 211;
    }

    public boolean isMapCold(Map map) {
        int mapId = map.mapId;
        return mapId >= 105 && mapId <= 110;
    }

    public boolean isMapAnhSang(Map map) {
        int mapId = map.mapId;
        return mapId >= 180 && mapId <= 183;
    }

    public boolean isMapBill(int mapId) {
        return mapId == 154;
    }

    public boolean isMapReverse(int mapId) {
        return mapId == 191;
    }

    public boolean isMapYadart(int mapId) {
        return mapId >= 131 && mapId <= 133;
    }

    public boolean isMapDoanhTrai(int mapId) {
        return mapId >= 53 && mapId <= 62;
    }

    public boolean isMapNguHanhSon(int mapId) {
        return mapId >= 122 && mapId <= 124;
    }

    public boolean isMapHTTV(int mapId) {
        return mapId >= 160 && mapId <= 163;
    }

    public boolean isMapBanDoKhoBau(int mapId) {
        return mapId >= 135 && mapId <= 138;
    }

    public boolean isMapKhongCoSieuQuai(int mapId) {
        return !isMapSetKichHoat(mapId)
                && mapId != 4 && mapId != 27 && mapId != 28
                && mapId != 12 && mapId != 31 && mapId != 32
                && mapId != 18 && mapId != 35 && mapId != 36;
    }

    public boolean isNamekPlanet(int mapId) {
        return mapId >= 7 && mapId <= 14;
    }

    public boolean isMapQuestTDST(int mapId) {
        return mapId == 79 || mapId == 82 || mapId == 83;
    }

    public boolean isMapSetKichHoat(int mapId) {
        return (mapId >= 1 && mapId <= 3) || (mapId == 8 || mapId == 9 || mapId == 11) || (mapId >= 15 && mapId <= 17);
    }

    public boolean isMapTuongLai(int mapId) {
        return (mapId >= 92 && mapId <= 94) || (mapId >= 96 && mapId <= 100) || mapId == 102 || mapId == 103;
    }
}
