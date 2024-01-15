package web.scraping.Project.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonTypeName("TableRow")
@JsonTypeInfo(include = As.WRAPPER_OBJECT, use = Id.NAME)
@JsonPropertyOrder(alphabetic = true)
public class TableRow {
    @JsonProperty
    private String symbol;

    @JsonProperty
    private String name;

    @JsonProperty
    private double price;

    @JsonProperty
    private double change;

    @JsonProperty
    private String percentChange;

    @JsonProperty
    private String volume;

    @JsonProperty
    private String avgVol3Month;

    @JsonProperty
    private String marketCap;

    @JsonProperty
    private String peRatioTTM;


}
