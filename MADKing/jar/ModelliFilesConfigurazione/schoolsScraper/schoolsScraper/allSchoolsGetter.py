"""
/**
 * 	MADKing allSchoolsGetter - Genera il file JSON con la lista
 *   delle scuole scaricando i dati automaticamente dal sito
 *   http://www.trampi.istruzione.it/vseata/action/promptSelectProvincia.do
 *   (No chance you need this program and do not speak Italian!)
 *  
 *   Copyright (C) 2016  Davide Gatto
 *   
 *   @author Davide Gatto
 *   @mail davgatto@gmail.com
 *   
 *   This file is part of MADKing
 *   MADKing is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

@author: Davide Gatto
@mail: davgatto@gmail.com

Modificare manualmente il campo 'provincia' e il campo 'numeroPagine',
che deve essere uguale al numero di pagine che compongono la tabella
relativa alla provincia desiderata sul sito
http://www.trampi.istruzione.it/vseata/action/promptSelectProvincia.do
(un giorno implementerò il rilevamento automatico)
"""
import json


PROVINCE = ["AL", "AN", "AR", "AP", "AT", "AV", "BA", "BL", "BN", "BG", "BI", "BS", "BR", "CA", "CL", "CB", "CE", "CT", "CZ", "CH", "CO", "CS", "CR", "KR", "CN", "EN", "FE", "FI", "FG", "FO", "FR", "GE", "GO", "GR", "IM", "IS", "AQ", "SP", "LT", "LE", "LC", "LI", "LO", "LU", "MC", "MN", "MS", "MT", "ME", "MI", "MO", "NA", "NO", "NU", "OR", "PD", "PA", "PR", "PV", "PG", "PS", "PE", "PC", "PI", "PT", "PN", "PZ", "PO", "RG", "RA", "RC", "RE", "RI", "RN", "RM", "RO", "SA", "SS", "SV", "SI", "SR", "SO", "TA", "TE", "TR", "TO", "TP", "TV", "TS", "UD", "VA", "VE", "VB", "VC", "VR", "VV", "VI"]

for prov in PROVINCE:
    filename = './allSchoolsFiles/'+ prov + '_allSchools.txt'
    IN = open(filename)
    lines = IN.readlines()
    IN.close()
   
    outputFile = './schoolsDetailsFiles/' + prov + "_schoolsDetails.json"
    OUT = open(outputFile, "w")
    OUT.write("[\n")
    
    i = 0
    for line in lines:
        l = json.dumps(str.strip(line[:-1])) + ',\n'
        if i % 6 == 0:
            if i > 0:
                OUT.write('\t,\n')
            l = str.lower(l)
            OUT.write('\t{\n' +
                      '\t\t"codMec": ' + l)
        elif i % 6 == 1:
            OUT.write('\t\t"tipo": ' + l)
        elif i % 6 == 2:
            OUT.write('\t\t"nome": ' + l)
        elif i % 6 == 3:
            OUT.write('\t\t"indirizzo": ' + l)
        elif i % 6 == 4:
            OUT.write('\t\t"cap": ' + l)
        elif i % 6 == 5:
            OUT.write('\t\t"comune": ' + l +
                      '\t\t"provincia": "' + prov + '"\n' +
                      '\t}\n')
        i += 1

    OUT.write("]")

    OUT.close()
