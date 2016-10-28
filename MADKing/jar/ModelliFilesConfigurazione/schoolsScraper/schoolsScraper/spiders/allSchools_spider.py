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
import scrapy

provincia = "TO" # TODO implementa passaggio 'provincia' da altro script
numeroPagine = 19 # TODO implementa rilevamento automatico

class QuotesSpider(scrapy.Spider):
    name = "allSchools"

    def start_requests(self):
        urls = []
        for i in range(1, numeroPagine + 1):
            s = str(i)
            urls.append('http://www.trampi.istruzione.it/vseata/action/promptSelectProvincia.do?selectProvincia=' + provincia + '&selectedPagina=' + s + '&CERCA')
        for url in urls:
            yield scrapy.Request(url=url, callback=self.parse)

    def parse(self, response):
        filename = provincia + '_allSchools.txt'
        f = open(filename, 'a')
        for entry in response.css("td")[12:]:
            s = str(entry.css('td').extract())
            if s.startswith('[\'<td style="vertical-align: top;">'):
                x = s[s.find('top;">')+6:s.find('<br>')]
                f.write(x.strip()+'\n')

