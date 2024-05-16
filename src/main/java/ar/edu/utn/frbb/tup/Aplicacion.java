package ar.edu.utn.frbb.tup;

import ar.edu.utn.frbb.tup.model.*;
import ar.edu.utn.frbb.tup.presentation.input.MenuInputProcessor;

class Aplicacion {

    public static void main(String args[]) {
        Banco banco = new Banco();

        MenuInputProcessor menuInputProcessor = new MenuInputProcessor();
        menuInputProcessor.renderMenu(banco);

    }


}
