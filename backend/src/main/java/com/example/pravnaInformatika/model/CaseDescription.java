package com.example.pravnaInformatika.model;

public class CaseDescription {
	
	
	private int id;
    private String court; // Sud
    private String businessNumber; // poslovniBroj, brojSlucaja
    private String judge; // Sudija
    private String defendant; // Okrivljeni
    private String felony; // Krivicno delo
    
    // 412
    private boolean fakeDocument; // Napravljen neistinit dokument
    private boolean publicDocument; // Napravljen neistinit javni dokument
    private boolean triedMaking; // Pokusaj pravljenja neistinitog dokumenta
    
    // 413
    private String signatureWithoutPermission; // Neovlasceno popunjene izjave koja imaju vrednost za pravne odnose
    private String fraudulentDocument; // Obmane sadrzaja dokumenta
    private String documentWithoutPermission; // Isprava izdata u ime drugog lica bez njegove saglasnosti
    private String fraudulentRank; // Ako se u svom potpisu stavi cin/rank/polozaj i ako nema taj cin/rank/polozaj
    private String sealWithoutPermission; // Koriscenje pravog pecata/znaka bez dozvole fizickog, pravnog (lica) ili neke institucije
    
    private String recorder; // Zapisnicar
    private String officialPerson; // Sluzbeno lice
    private String punishment; // Kazna

}
