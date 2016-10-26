# -*- coding: utf-8 -*-
"""
/**
 * 	MADKing schoolsDetail.json maker - Genera il file JSON con la lista
 *   delle scuole a partire da un file di testo strutturato opportunamente
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

inputFile deve contenere solo entrate del tipo:
----------------------------------
CSPS310001
	LICEO SCIENTIFICO
	LS TREBISACCE+SEZ. CL. ANN.
	VIALE DELLA LIBERTA' S.N.C.
	87075
	TREBISACCE
----------------------------------
cioè quel che viene fuori facendo copia e incolla su un file di testo
dalle tabelle del sito del MIUR:
http://www.trampi.istruzione.it/vseata/action/promptSelectProvincia.do

provincia deve essere la targa della provincia (i risultati sul sito
sono divisi per provincia)

"""
import json
provincia = "CS"

inputFile = "/home/dave/Desktop/CS_allSchools.txt"

outputFile = "schoolsDetails.json"

IN = open(inputFile)
OUT = open(outputFile, "w")

OUT.write("[\n")

lines = IN.readlines()

IN.close()

i = 0
for line in lines:
    if i % 6 == 0:
        l = json.dumps(str.strip(line[:-1])) + ',\n'
    else:
        l = json.dumps(str.strip(line[1:-1])) + ',\n'

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
                  '\t\t"provincia": "' + provincia + '"\n' +
                  '\t}\n')
    i += 1

OUT.write("\n]")

OUT.close()
