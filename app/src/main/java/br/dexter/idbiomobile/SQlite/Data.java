package br.dexter.idbiomobile.SQlite;

public class Data
{
    private long id;
    private String local, localidade;
    private String city, state, data, resultado;
    private float latitude, longitude;

    public String getData() {return data;}
    public void setData(String data) {this.data = data;}

    public String getLocal() {return local;}
    public void setLocal(String local) {this.local = local;}

    public String getLocalidade() {return localidade;}
    public void setLocalidade(String localidade) {this.localidade = localidade;}

    float getLatitude() {return latitude;}
    public void setLatitude(float latitude) {this.latitude = latitude;}

    float getLongitude() {return longitude;}
    public void setLongitude(float longitude) {this.longitude = longitude;}

    String getCity() {return city;}
    public void setCity(String city) {this.city = city;}

    String getState() {return state;}
    public void setState(String state) {this.state = state;}

    public long getId() {return id;}
    public void setId(long id) {this.id = id;}

    public String getResultado() {return resultado;}
    public void setResultado(String resultado) {this.resultado = resultado;}
}
