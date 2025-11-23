// j'ai commence par faire ca page chargee
console.log("page chargee ok");

// variable pour stocker les valeurs des portes
var valeurs = {
    et: { a: 0, b: 0 },
    ou: { a: 0, b: 0 },
    xor: { a: 0, b: 0 },
    nand: { a: 0, b: 0 },
    nor: { a: 0, b: 0 },
    not: { a: 0 }
};

console.log("valeurs de depart:", valeurs);

// fonction pour changer la valeur quand on clique sur un bouton
function changerValeur(typePorte, entree) {
    console.log("clic sur porte " + typePorte + " entree " + entree);
    console.log("valeur avant: " + valeurs[typePorte][entree]);

    // on inverse la valeur
    if (valeurs[typePorte][entree] == 0) {
        valeurs[typePorte][entree] = 1;
    } else {
        valeurs[typePorte][entree] = 0;
    }

    console.log("valeur apres: " + valeurs[typePorte][entree]);

    // on met a jour le texte du bouton
    var idBouton = typePorte + "-" + entree;
    var leBouton = document.getElementById(idBouton);
    leBouton.textContent = valeurs[typePorte][entree];

    // on change la couleur si c'est 1
    if (valeurs[typePorte][entree] == 1) {
        leBouton.className = "actif";
    } else {
        leBouton.className = "";
    }

    // maintenant on calcule la sortie
    calculer(typePorte);
}

// fonction speciale pour NOT car une seule entree
function changerValeurNot() {
    console.log("clic sur NOT");
    console.log("valeur avant: " + valeurs.not.a);

    if (valeurs.not.a == 0) {
        valeurs.not.a = 1;
    } else {
        valeurs.not.a = 0;
    }

    console.log("valeur apres: " + valeurs.not.a);

    var leBouton = document.getElementById("not-a");
    leBouton.textContent = valeurs.not.a;

    if (valeurs.not.a == 1) {
        leBouton.className = "actif";
    } else {
        leBouton.className = "";
    }

    calculerNot();
}

// fonction qui calcule le resultat
function calculer(typePorte) {
    console.log("calcul pour " + typePorte);

    var valA = valeurs[typePorte].a;
    var valB = valeurs[typePorte].b;
    var resultat = 0;

    console.log("A=" + valA + " B=" + valB);

    // porte ET
    if (typePorte == "et") {
        if (valA == 1 && valB == 1) {
            resultat = 1;
        }
        console.log("porte ET resultat=" + resultat);
    }

    // porte OU
    if (typePorte == "ou") {
        if (valA == 1 || valB == 1) {
            resultat = 1;
        }
        console.log("porte OU resultat=" + resultat);
    }

    // porte XOR
    if (typePorte == "xor") {
        if (valA != valB) {
            resultat = 1;
        }
        console.log("porte XOR resultat=" + resultat);
    }

    // porte NAND
    if (typePorte == "nand") {
        if (valA == 1 && valB == 1) {
            resultat = 0;
        } else {
            resultat = 1;
        }
        console.log("porte NAND resultat=" + resultat);
    }

    // porte NOR
    if (typePorte == "nor") {
        if (valA == 0 && valB == 0) {
            resultat = 1;
        } else {
            resultat = 0;
        }
        console.log("porte NOR resultat=" + resultat);
    }

    // on affiche le resultat
    var elementSortie = document.getElementById("sortie-" + typePorte);
    elementSortie.textContent = resultat;
    console.log("affichage ok");
}

// calcul pour NOT
function calculerNot() {
    console.log("calcul NOT");

    var valA = valeurs.not.a;
    var resultat = 0;

    if (valA == 0) {
        resultat = 1;
    } else {
        resultat = 0;
    }

    console.log("NOT resultat=" + resultat);

    var elementSortie = document.getElementById("sortie-not");
    elementSortie.textContent = resultat;
}

// fonction pour voir la taille de l'ecran
function verifierTaille() {
    var largeur = window.innerWidth;
    var appareil = "";

    if (largeur <= 480) {
        appareil = "mobile";
    } else if (largeur <= 768) {
        appareil = "tablette";
    } else {
        appareil = "ordi";
    }

    console.log("appareil: " + appareil + " largeur: " + largeur);
}

// quand la page est chargee
window.addEventListener("load", function () {
    console.log("tout est charge");
    verifierTaille();
});

// si on redimensionne la fenetre
window.addEventListener("resize", function () {
    verifierTaille();
});
