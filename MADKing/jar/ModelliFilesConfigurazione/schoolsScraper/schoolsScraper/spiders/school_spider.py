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

"""
from __future__ import absolute_import
import scrapy
import province


class AllSchoolsSpider(scrapy.Spider):
    name = "allSchools"
    # provincia = 'CZ'
    # numeroPagine = 5

    def __init__(self, provincia='CZ', numeroPagine=5, *args, **kwargs):
        super(AllSchoolsSpider, self).__init__(*args, **kwargs)
        self.provincia = provincia
        self.numeroPagine = int(numeroPagine)

    def start_requests(self):
        urls = []
        for prov in province.PROVINCE:
            for i in range(1, province.PAGINE[prov] + 1):
                s = str(i)
                urls.append('http://www.trampi.istruzione.it/vseata/action/promptSelectProvincia.do?selectProvincia=' + prov + '&selectedPagina=' + s + '&CERCA')
        for url in urls:
            yield scrapy.Request(url=url, callback=self.parse)

    def parse(self, response):
        sp = str(response.css('body').css('form').css('input').extract()[1])
        p = sp[sp.find('value="')+7:sp.find('value="')+9]
        filename = p + '_allSchools.txt'
        f = open(filename, 'a')
        for entry in response.css("td")[12:]:
            s = str(entry.css('td').extract())
            if s.startswith('[\'<td style="vertical-align: top;">'):
                x = s[s.find('top;">')+6:s.find('<br>')]
                f.write(x.strip()+'\n')

