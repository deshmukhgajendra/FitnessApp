package com.example.myfitnessapp.Database;

public class NutrientMapping {

    public static String getNutrientName(int attrId){

        switch (attrId){
            case 203: return "Protein";
            case 204: return "Total lipid (fat)";
            case 205: return "Carbohydrate";
            case 207: return "Ash";
            case 208: return "Energy";
            case 209: return "Starch";
            case 210: return "Sucrose";
            case 211: return "Glucose";
            case 212: return "Fructose";
            case 213: return "Lactose";
            case 214: return "Maltose";
            case 221: return "Alcohol";
            case 255: return "Water";
            case 262: return "Caffeine";
            case 263: return "Theobromine";
            case 268: return "Energy";
            case 269: return "Sugar";
            case 291: return "Fiber";
            case 301: return "Calcium";
            case 303: return "Iron";
            case 304: return "Magnesium";
            case 305: return "Phosphorus";
            case 306: return "Potassium";
            case 307: return "Sodium";
            case 309: return "Zinc";
            case 312: return "Copper";
            case 313: return "Fluoride";
            case 315: return "Manganese";
            case 317: return "Selenium";
            case 318: return "Vitamin A";
            case 319: return "Retinol";
            case 320: return "Vitamin A";
            case 321: return "Beta Carotene";
            case 322: return "Alpha Carotene";
            case 323: return "Vitamin E";
            case 324: return "Vitamin D (D2 + D3)";
            case 328: return "Vitamin D";
            case 334: return "Beta Cryptoxanthin";
            case 337: return "Lycopene";
            case 338: return "Lutein + zeaxanthin";
            case 341: return "Gamma Tocopherol";
            case 342: return "Delta Tocopherol";
            case 343: return "Beta Tocopherol";
            case 401: return "Vitamin C";
            case 404: return "Thiamin";
            case 405: return "Riboflavin";
            case 406: return "Niacin";
            case 410: return "Pantothenic acid";
            case 415: return "Vitamin B-6";
            case 417: return "Folate";
            case 418: return "Vitamin B-12";
            case 421: return "Choline";
            case 429: return "Menaquinone-4";
            case 430: return "Vitamin K (phylloquinone)";
            case 431: return "Folate";
            case 432: return "Folate, DFE";
            case 435: return "Folate, DFE";
            case 454: return "Folic acid";
            case 501: return "Tryptophan";
            case 502: return "Threonine";
            case 503: return "Isoleucine";
            case 504: return "Leucine";
            case 505: return "Lysine";
            case 506: return "Methionine";
            case 507: return "Cystine";
            case 508: return "Phenylalanine";
            case 509: return "Tyrosine";
            case 510: return "Valine";
            case 511: return "Arginine";
            case 512: return "Histidine";
            case 513: return "Alanine";
            case 514: return "Aspartic acid";
            case 515: return "Glutamic acid";
            case 516: return "Glycine";
            case 517: return "Proline";
            case 518: return "Serine";
            case 601: return "Cholesterol";
            case 605: return "Trans Fatty acids";
            case 606: return "Saturated Fatty acids";
            case 607: return "Butyric acid";
            case 608: return "Caproic acid";
            case 609: return "Caprylic acid";
            case 610: return "Capric acid";
            case 611: return "Lauric acid";
            case 612: return "Myristic acid";
            case 613: return "Palmitic acid";
            case 614: return "Stearic acid";
            case 617: return "Oleic acid";
            case 618: return "Linoleic acid";
            case 619: return "Alpha-Linolenic acid";
            case 620: return "Arachidonic acid";
            case 621: return "Docosahexaenoic acid, DHA";
            case 626: return "Myristoleic acid";
            case 627: return "Palmitoleic acid";
            case 628: return "cis-Vaccenic acid";
            case 629: return "Linoleic acid";
            case 630: return "Alpha-Linolenic acid";
            case 631: return "Eicosenoic acid";
            case 636: return "Docosahexaenoic acid";
            case 645: return "Monosaturated Fatty acids";
            case 646: return "Polysaturated Fatty acids";
            default: return "Unknown Nutrient";
        }
    }
}
