package com.example.notificationservice.models.Blacklist;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;
import lombok.NonNull;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "blacklist_record")
public class BlacklistRecord {

    @NonNull
    @Id
    private String phoneNumber;
}
