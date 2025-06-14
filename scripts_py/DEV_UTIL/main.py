import os
import GEN_SQL_EXAM as gen_s
import merge_geojsons as gen_c

#gen_s.generate_sql_from_txt("../../logs/generated_files.txt", "../../src/main/resources/db/init/init_db_danger_record_Rape .sql", 2)
gen_c.GenerateCustomModelUseByGeoJson().generate_custom_model(
    folder_path = "../../content/geojson/광주광역시",
    base_template= "foot_shortest",
    avoidance_type="block",
    output_file="foot_shortest_avoid_zone.json"
)