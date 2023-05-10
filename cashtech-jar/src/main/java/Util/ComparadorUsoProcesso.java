/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Util;

import com.github.britooo.looca.api.group.processos.Processo;
import java.util.Comparator;

/**
 *
 * @author murilo
 */
public class ComparadorUsoProcesso implements Comparator<Processo> {

    @Override
    public int compare(Processo p1, Processo p2) {
        return -1 * Double.compare(p1.getUsoCpu(), p2.getUsoCpu());
    }
}   
