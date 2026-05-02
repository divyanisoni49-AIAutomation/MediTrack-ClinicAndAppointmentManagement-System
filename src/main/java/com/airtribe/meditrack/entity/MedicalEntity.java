package com.airtribe.meditrack.entity;

public class MedicalEntity {
        private String id;
        private String name;

        public MedicalEntity(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() { return id; }
        public String getName() { return name; }
}
