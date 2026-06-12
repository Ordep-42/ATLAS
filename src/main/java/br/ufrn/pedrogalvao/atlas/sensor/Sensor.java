package br.ufrn.pedrogalvao.atlas.sensor;

import jakarta.persistence.*;

@Entity
@Table(
	name = "sensors",
    indexes = {
        @Index(
            name = "idx_sensor_mission",
            columnList = "missionId"
        )
    }
)
public class Sensor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private Long missionId;
	private String name;
	
	@Enumerated(EnumType.STRING)
	private SensorType type;
	private String unit;
	
	public Sensor(){}
	
	public Sensor(Long missionId, String name, SensorType type, String unit) {
        this.missionId = missionId;
        this.name = name;
        this.type = type;
        this.unit = unit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMissionId() {
        return missionId;
    }

    public void setMissionId(Long missionId) {
        this.missionId = missionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SensorType getType() {
        return type;
    }

    public void setType(SensorType type) {
        this.type = type;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
