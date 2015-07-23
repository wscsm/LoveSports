package mobi.imuse.pickview.city;

import java.util.List;

/**
 * Created by suyanlu on 15/7/23.
 */
public class CityJsonModel {

    private List<ProvincesEntity> Provinces;

    public void setProvinces(List<ProvincesEntity> Provinces) {
        this.Provinces = Provinces;
    }

    public List<ProvincesEntity> getProvinces() {
        return Provinces;
    }

    public static class ProvincesEntity {
        private String Name;
        private List<CitiesEntity> Cities;

        public void setName(String Name) {
            this.Name = Name;
        }

        public void setCities(List<CitiesEntity> Cities) {
            this.Cities = Cities;
        }

        public String getName() {
            return Name;
        }

        public List<CitiesEntity> getCities() {
            return Cities;
        }

        public static class CitiesEntity {
            /**
             * Name : 东城区
             * CityWeatherCode : 101010100
             */
            private String Name;
            private String CityWeatherCode;

            public void setName(String Name) {
                this.Name = Name;
            }

            public void setCityWeatherCode(String CityWeatherCode) {
                this.CityWeatherCode = CityWeatherCode;
            }

            public String getName() {
                return Name;
            }

            public String getCityWeatherCode() {
                return CityWeatherCode;
            }
        }
    }
}
