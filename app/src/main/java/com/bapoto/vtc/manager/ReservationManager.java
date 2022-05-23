package com.bapoto.vtc.manager;

import com.bapoto.vtc.repository.ReservationRepository;

public class ReservationManager {
    private static volatile ReservationManager instance;
    private final ReservationRepository reservationRepository;


    public ReservationManager() {
        reservationRepository = ReservationRepository.getInstance();
    }

    public static ReservationManager getInstance() {
        ReservationManager result = instance;
        if (result != null) {
            return result;
        }
        synchronized (ReservationRepository.class) {
            if (instance == null) {
                instance = new ReservationManager();
            }
            return instance;
        }
    }

    public void createReservation(String nom, String tel, String desti, String rdv, String date,
                                  String heure, String infos) {
        reservationRepository.createReservation(nom,tel,desti,rdv,date,heure,infos);
    }

    public void getAllUserReservation( ){

        reservationRepository.getCurrentUserReservations();


    }
}



