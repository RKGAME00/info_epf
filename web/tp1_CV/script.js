function detecter_taille_ecran(){
var largeur_fenetre=window.innerWidth;
var type_appareil="";
if(largeur_fenetre<=480){type_appareil="mobile";}
else if(largeur_fenetre<=768){type_appareil="tablette";}
else{type_appareil="ordinateur";}
console.log("Type appareil: "+type_appareil);
console.log("Largeur: "+largeur_fenetre+"px");
return type_appareil;
}

function adapter_contenu(){
var type_appareil=detecter_taille_ecran();
var conteneur=document.getElementById("conteneur");
conteneur.className=type_appareil;
}

window.addEventListener("load",function(){
console.log("TP1 CV Page chargee (je sais utiliser la console)");
adapter_contenu();
});

window.addEventListener("resize",function(){
adapter_contenu();
});