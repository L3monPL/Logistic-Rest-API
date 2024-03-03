package net.l3mon.LogisticsL3mon.UserAuth.entity;

public enum Code {
    SUCCESS("Operacja zakończona sukcesem"),
    PERMIT("Przyznano dostep"),
    A1("Nie udało się zalogować"),
    A2("Użytkownik o wskazanej nazwie nie istnieje"),
    A3("Wskazany token jest pusty lub nie ważny"),A4("Użytkownik o podanej nazwie juz istnieje"),
    A5("Użytkownik o podanmym mailu juz istnieje"),
    A6("Użytkownik nie istnieje"),
    A7("Nie przypisano użytkownika firmy");



    public final String label;
    private Code(String label){
        this.label = label;
    }
}
