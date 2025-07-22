package nro.server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import nro.models.map.EffectMap;
import nro.models.map.MapTemplate;
import nro.models.map.WayPoint;
import nro.server.Manager;
import static nro.server.Manager.MAP_TEMPLATES;
import nro.utils.Log;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class OptimizedDatabaseLoader {
    private static final JSONParser jsonParser = new JSONParser();

    public static void loadMapTemplates(Connection con) throws SQLException, Exception {
        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM map_template");
             ResultSet rs = ps.executeQuery()) {

            List<MapTemplate> mapTemplates = new ArrayList<>();

            while (rs.next()) {
                MapTemplate mapTemplate = new MapTemplate();
                mapTemplate.id = rs.getInt("id");
                mapTemplate.name = rs.getString("name");

                loadMapData(mapTemplate, rs.getString("data"));
                loadWaypoints(mapTemplate, rs.getString("waypoints"));
                loadMobs(mapTemplate, rs.getString("mobs"));
                loadNpcs(mapTemplate, rs.getString("npcs"));
//                loadEffects(mapTemplate, rs.getString("effect"));

                mapTemplates.add(mapTemplate);
            }

            MAP_TEMPLATES = mapTemplates.toArray(new MapTemplate[0]);
            Log.success("Load map template thành công (" + MAP_TEMPLATES.length + ")");
        }
    }

    private static void loadMapData(MapTemplate mapTemplate, String data) throws Exception {
        JSONArray dataArray = (JSONArray) jsonParser.parse(data);
        mapTemplate.type = Byte.parseByte(dataArray.get(0).toString());
        mapTemplate.planetId = Byte.parseByte(dataArray.get(1).toString());
        mapTemplate.bgType = Byte.parseByte(dataArray.get(2).toString());
        mapTemplate.tileId = Byte.parseByte(dataArray.get(3).toString());
        mapTemplate.bgId = Byte.parseByte(dataArray.get(4).toString());
    }

    private static void loadWaypoints(MapTemplate mapTemplate, String waypoints) throws Exception {
        JSONArray dataArray = (JSONArray) jsonParser.parse(waypoints.replaceAll("\\[\"\\[", "[[")
                                                                   .replaceAll("\\]\"\\]", "]]")
                                                                   .replaceAll("\",\"", ","));
        mapTemplate.wayPoints = new ArrayList<>();
        for (Object obj : dataArray) {
            JSONArray wpData = (JSONArray) obj;
            WayPoint wp = new WayPoint();
            wp.name = wpData.get(0).toString();
            wp.minX = Short.parseShort(wpData.get(1).toString());
            wp.minY = Short.parseShort(wpData.get(2).toString());
            wp.maxX = Short.parseShort(wpData.get(3).toString());
            wp.maxY = Short.parseShort(wpData.get(4).toString());
            wp.isEnter = Byte.parseByte(wpData.get(5).toString()) == 1;
            wp.isOffline = Byte.parseByte(wpData.get(6).toString()) == 1;
            wp.goMap = Short.parseShort(wpData.get(7).toString());
            wp.goX = Short.parseShort(wpData.get(8).toString());
            wp.goY = Short.parseShort(wpData.get(9).toString());
            mapTemplate.wayPoints.add(wp);
        }
    }

    private static void loadMobs(MapTemplate mapTemplate, String mobs) throws Exception {
        JSONArray dataArray = (JSONArray) jsonParser.parse(mobs.replaceAll("\\\"", ""));
        int size = dataArray.size();
        mapTemplate.mobTemp = new byte[size];
        mapTemplate.mobLevel = new byte[size];
        mapTemplate.mobHp = new long[size];
        mapTemplate.mobX = new short[size];
        mapTemplate.mobY = new short[size];

        for (int i = 0; i < size; i++) {
            JSONArray mobData = (JSONArray) dataArray.get(i);
            mapTemplate.mobTemp[i] = Byte.parseByte(mobData.get(0).toString());
            mapTemplate.mobLevel[i] = Byte.parseByte(mobData.get(1).toString());
            mapTemplate.mobHp[i] = Long.parseLong(mobData.get(2).toString());
            mapTemplate.mobX[i] = Short.parseShort(mobData.get(3).toString());
            mapTemplate.mobY[i] = Short.parseShort(mobData.get(4).toString());
        }
    }

    private static void loadNpcs(MapTemplate mapTemplate, String npcs) throws Exception {
        JSONArray dataArray = (JSONArray) jsonParser.parse(npcs.replaceAll("\\\"", ""));
        int size = dataArray.size();
        mapTemplate.npcId = new byte[size];
        mapTemplate.npcX = new short[size];
        mapTemplate.npcY = new short[size];
        mapTemplate.npcAvatar = new short[size];

        for (int i = 0; i < size; i++) {
            JSONArray npcData = (JSONArray) dataArray.get(i);
            mapTemplate.npcId[i] = Byte.parseByte(npcData.get(0).toString());
            mapTemplate.npcX[i] = Short.parseShort(npcData.get(1).toString());
            mapTemplate.npcY[i] = Short.parseShort(npcData.get(2).toString());
            mapTemplate.npcAvatar[i] = Short.parseShort(npcData.get(3).toString());
        }
    }

    private static void loadEffects(MapTemplate mapTemplate, String effects) throws Exception {
        JSONArray dataArray = (JSONArray) jsonParser.parse(effects);
        mapTemplate.effectMaps = new ArrayList<>();
        for (Object obj : dataArray) {
            if (obj instanceof JSONObject) {
                JSONObject effectData = (JSONObject) obj;
                EffectMap em = new EffectMap();
                
                Object keyObj = effectData.get("key");
                Object valueObj = effectData.get("value");
                
                em.setKey(keyObj != null ? keyObj.toString() : "");
                em.setValue(valueObj != null ? valueObj.toString() : "");
                
                mapTemplate.effectMaps.add(em);
            }
        }

        if (Manager.EVENT_SEVER == 3) {
            EffectMap em = new EffectMap();
            em.setKey("beff");
            em.setValue("11");
            mapTemplate.effectMaps.add(em);
        }
    }

}