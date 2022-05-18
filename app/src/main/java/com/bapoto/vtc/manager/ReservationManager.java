package com.bapoto.vtc.manager;

import com.bapoto.vtc.model.Reservation;
import com.bapoto.vtc.repository.ReservationRepository;

import java.util.UUID;

public class ReservationManager {
    private static volatile ReservationManager instance;
    private final ReservationRepository reservationRepository;


    public ReservationManager( ) { reservationRepository =  new ReservationRepository();
    }

    public static ReservationManager getInstance() {
        ReservationManager result = instance;
        if (result != null) {
            return result;
        }
        synchronized(ReservationRepository.class) {
            if (instance == null) {
                instance = new ReservationManager();
            }
            return instance;
        }
    }

  /*  public void createReservation(String nom,String tel,String desti,String rdv,String date,
                                  String heure,String infos) {

        Reservation reservation = new Reservation();
        nom = reservation.getName();
        tel = reservation.getTelephone();
        desti = reservation.getDestination();
        rdv = reservation.getRdv();
        date = reservation.getDate();
        heure = reservation.getHour();
        infos = reservation.getInfos();

        reservationRepository.createReservation(nom, tel, desti, rdv, date, heure, infos);*/


    }



