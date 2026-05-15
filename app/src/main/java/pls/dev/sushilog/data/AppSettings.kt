package pls.dev.sushilog.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.core.content.edit
import java.util.Locale

/** Temas visuales disponibles: Oscuro, Claro y Salmón. */
enum class AppTheme(val id: String, val displayName: Map<AppLanguage, String>) {
    DARK("dark", mapOf(AppLanguage.SPANISH to "Oscuro", AppLanguage.ENGLISH to "Dark", AppLanguage.FRENCH to "Sombre", AppLanguage.ITALIAN to "Scuro")),
    LIGHT("light", mapOf(AppLanguage.SPANISH to "Claro", AppLanguage.ENGLISH to "Light", AppLanguage.FRENCH to "Clair", AppLanguage.ITALIAN to "Chiaro")),
    SALMON("salmon", mapOf(AppLanguage.SPANISH to "Salmón", AppLanguage.ENGLISH to "Salmon", AppLanguage.FRENCH to "Saumon", AppLanguage.ITALIAN to "Salmone"))
}

/** Idiomas soportados con detección automática del dispositivo. */
enum class AppLanguage(val code: String, val displayName: String, val flag: String, val flagRes: Int) {
    SPANISH("es", "Español", "🇪🇸", pls.dev.sushilog.R.drawable.spanish),
    ENGLISH("en", "English", "🇬🇧", pls.dev.sushilog.R.drawable.english),
    FRENCH("fr", "Français", "🇫🇷", pls.dev.sushilog.R.drawable.french),
    ITALIAN("it", "Italiano", "🇮🇹", pls.dev.sushilog.R.drawable.italian);

    companion object {
        fun fromDeviceLocale(): AppLanguage {
            return when (Locale.getDefault().language.lowercase()) {
                "es" -> SPANISH
                "fr" -> FRENCH
                "it" -> ITALIAN
                else -> ENGLISH
            }
        }
    }
}

/** Sistema de internacionalización con todos los textos en 4 idiomas. */
object AppStrings {

    val monthNames: Map<AppLanguage, List<String>> = mapOf(
        AppLanguage.SPANISH to listOf("enero","febrero","marzo","abril","mayo","junio","julio","agosto","septiembre","octubre","noviembre","diciembre"),
        AppLanguage.ENGLISH to listOf("January","February","March","April","May","June","July","August","September","October","November","December"),
        AppLanguage.FRENCH  to listOf("janvier","février","mars","avril","mai","juin","juillet","août","septembre","octobre","novembre","décembre"),
        AppLanguage.ITALIAN to listOf("gennaio","febbraio","marzo","aprile","maggio","giugno","luglio","agosto","settembre","ottobre","novembre","dicembre")
    )

    data class Strings(
        val back: String, val save: String, val exit: String, val cancel: String,
        val delete: String, val share: String, val confirm: String,
        val add: String, val edit: String, val name: String,
        val begin: String, val history: String, val stats: String,
        val achievements: String, val settings: String,
        val darkTheme: String, val lightTheme: String, val salmonTheme: String,
        val tagline: String,
        val newSession: String, val whereAreYouEating: String, val restaurantName: String,
        val start: String, val endSession: String, val finishSession: String,
        val exitDialogTitle: String, val exitDialogMessage: String,
        val totalPieces: String, val continueStr: String, val noName: String, val total: String,
        val historyTitle: String, val noHistory: String, val noHistoryDesc: String,
        val sessions: String, val session: String, val pieces: String, val piece: String,
        val deleteSession: String, val deleteSessionConfirm: String,
        val today: String, val yesterday: String,
        val daysAgo: String, val weeksAgo: String, val monthsAgo: String, val others: String,
        val statsTitle: String, val noData: String, val noDataDesc: String,
        val totalPiecesLabel: String, val totalPiecesLabelSingular: String, val sessionCount: String,
        val average: String, val record: String, val breakdown: String,
        val curiosities: String, val riceApprox: String, val favoritePiece: String, val salmonApprox: String,
        val caloriesApprox: String,
        val achievementsTitle: String, val achievementsUnlocked: String, val unlocked: String,
        val achTotal100Title: String, val achTotal500Title: String,
        val achTotal1000Title: String, val achTotal5000Title: String,
        val achSession30Title: String, val achSession50Title: String, val achSession100Title: String,
        val achNigiri100Title: String, val achSashimi100Title: String,
        val achMaki100Title: String, val achGyoza50Title: String,
        val achNigiriSession30Title: String, val achSashimiSession20Title: String,
        val achSessions5Title: String, val achSessions25Title: String, val achSessions50Title: String,
        val achVariety6Title: String, val achVarietyAllTitle: String,
        val achAllInOneTitle: String, val achFirstSessionTitle: String,
        val achTotal100Desc: String, val achTotal500Desc: String,
        val achTotal1000Desc: String, val achTotal5000Desc: String,
        val achSession30Desc: String, val achSession50Desc: String, val achSession100Desc: String,
        val achNigiri100Desc: String, val achSashimi100Desc: String,
        val achMaki100Desc: String, val achGyoza50Desc: String,
        val achNigiriSession30Desc: String, val achSashimiSession20Desc: String,
        val achSessions5Desc: String, val achSessions25Desc: String, val achSessions50Desc: String,
        val achVariety6Desc: String, val achVarietyAllDesc: String,
        val achAllInOneDesc: String, val achFirstSessionDesc: String,
        val settingsTitle: String, val appearance: String, val appTheme: String, val language: String,
        val deleteAll: String, val deleteAllSubtitle: String,
        val information: String, val version: String, val developedBy: String,
        val deleteAllConfirmTitle: String, val deleteAllConfirmMsg: String,
        val deleteAllConfirmBtn: String, val dataDeleted: String,
        val customPieces: String, val customPiecesSubtitle: String,
        val addCustomPiece: String, val customPieceName: String, val customPieceNameHint: String,
        val noPieceName: String, val deleteCustomPiece: String, val deleteCustomPieceConfirm: String,
        val customPiecesManage: String, val customPiecesEmpty: String, val customPiecesLimit: String,
        val deletedPiece: String, val customPieceKcalHint: String, val customPieceSalmonHint: String,
        val customPieceRiceHint: String, val customPieceSalmonLabel: String, val historyKcalLabel: String,
        val historyRiceLabel: String, val historySalmonLabel: String, val historySalmonLabelSingular: String, val statsWholeSalmon: String,
        val statsSalmonPieces: String, val statsSalmonPiecesSingular: String, val shareActionTitle: String, val shareIntentText: String,
        val shareWatermark: String, val rice: String, val kcal: String, val explore: String,
        val catTrajectory: String, val catAccumulation: String, val catFeats: String, val catSpecialist: String, val catExplorer: String,
        val duplicatePieceName: String,
        val contact: String
    )

    private val spanish = Strings(
        back="Volver", save="Guardar", exit="Salir", cancel="Cancelar",
        delete="Eliminar", share="Compartir", confirm="Confirmar", add="Añadir", edit="Editar", name="Nombre",
        begin="COMENZAR", history="HISTORIAL", stats="ESTADÍSTICAS", achievements="LOGROS", settings="Ajustes",
        darkTheme="Oscuro", lightTheme="Claro", salmonTheme="Salmón",
        tagline="Hora de comer sushi",
        newSession="Nueva sesión", whereAreYouEating="¿Dónde estás comiendo?", restaurantName="Nombre del restaurante...",
        start="Empezar", endSession="TERMINAR SESIÓN", finishSession="¿Terminar sesión?",
        exitDialogTitle="¿Salir de la sesión?", exitDialogMessage="Si sales ahora perderás el progreso actual.",
        totalPieces="piezas en total", continueStr="Continuar", noName="Sin nombre", total="Total",
        historyTitle="Historial", noHistory="Sin historial", noHistoryDesc="Completa tu primera sesión\npara ver tu historial aquí",
        sessions="sesiones", session="sesión", pieces="piezas", piece="pieza",
        deleteSession="¿Eliminar sesión?", deleteSessionConfirm="Se eliminará la sesión con %d piezas.",
        today="Hoy", yesterday="Ayer", daysAgo="Hace %d días", weeksAgo="Hace %d semanas", monthsAgo="Hace %d meses", others="Otros",
        statsTitle="Estadísticas", noData="Sin datos", noDataDesc="Completa sesiones para\nver tus estadísticas",
        totalPiecesLabel="piezas totales", totalPiecesLabelSingular="pieza total", sessionCount="Sesiones", average="Promedio", record="Récord",
        breakdown="Desglose por tipo", curiosities="Curiosidades",
        riceApprox="Has comido aprox. %dg de arroz", favoritePiece="Tu pieza favorita es: %s", salmonApprox="Aproximadamente %d salmones enteros",
        caloriesApprox="Has consumido unas %d kcal",
        achievementsTitle="Logros", achievementsUnlocked="logros completados", unlocked="Desbloqueado",
        achTotal100Title="Principiante", achTotal500Title="Aficionado", achTotal1000Title="Experto", achTotal5000Title="Maestro Sushi",
        achSession30Title="Buen apetito", achSession50Title="Hambre voraz", achSession100Title="Máquina de comer",
        achNigiri100Title="Fan del Nigiri", achSashimi100Title="Amante del Sashimi", achMaki100Title="Loco por el Maki", achGyoza50Title="Gyoza Lover",
        achNigiriSession30Title="Rey del Nigiri", achSashimiSession20Title="Sashimi Master",
        achSessions5Title="Hábito", achSessions25Title="Cliente VIP", achSessions50Title="Leyenda del buffet",
        achVariety6Title="Explorador", achVarietyAllTitle="Catador completo", achAllInOneTitle="El completista", achFirstSessionTitle="Primera vez",
        achTotal100Desc="Come 100 piezas en total", achTotal500Desc="Come 500 piezas en total",
        achTotal1000Desc="Come 1000 piezas en total", achTotal5000Desc="Come 5000 piezas en total",
        achSession30Desc="Come 30 piezas en una sesión", achSession50Desc="Come 50 piezas en una sesión", achSession100Desc="Come 100 piezas en una sesión",
        achNigiri100Desc="Come 100 nigiris en total", achSashimi100Desc="Come 100 sashimis en total",
        achMaki100Desc="Come 100 makis en total", achGyoza50Desc="Come 50 gyozas en total",
        achNigiriSession30Desc="Come 30 nigiris en una sesión", achSashimiSession20Desc="Come 20 sashimis en una sesión",
        achSessions5Desc="Completa 5 sesiones", achSessions25Desc="Completa 25 sesiones", achSessions50Desc="Completa 50 sesiones",
        achVariety6Desc="Prueba 6 tipos diferentes de piezas", achVarietyAllDesc="Prueba todos los tipos de piezas",
        achAllInOneDesc="Come al menos 1 de cada tipo en una sesión", achFirstSessionDesc="Completa tu primera sesión",
        settingsTitle="Ajustes", appearance="Apariencia", appTheme="Tema de la app", language="Idioma",
        deleteAll="Borrar todos los datos", deleteAllSubtitle="Elimina todo tu historial",
        information="Información", version="Versión", developedBy="Desarrollado por",
        deleteAllConfirmTitle="¿Borrar todos los datos?",
        deleteAllConfirmMsg="Esta acción no se puede deshacer. Se eliminarán todas tus sesiones, estadísticas y logros.",
        deleteAllConfirmBtn="Borrar todo", dataDeleted="Datos eliminados",
        customPieces="Piezas personalizadas", customPiecesSubtitle="Añade tus propios tipos de sushi",
        addCustomPiece="Añadir pieza", customPieceName="Nombre de la pieza", customPieceNameHint="Ej: Onigiri, Futomaki...",
        noPieceName="El nombre no puede estar vacío", deleteCustomPiece="¿Eliminar pieza?",
        deleteCustomPieceConfirm="Se eliminará \"%s\" de tus piezas personalizadas.",
        customPiecesManage="Gestionar piezas personalizadas", customPiecesEmpty="No tienes piezas personalizadas aún",
        customPiecesLimit="Límite de 12 piezas personalizadas alcanzado",
        deletedPiece="Pieza eliminada", customPieceKcalHint="aprox.", customPieceSalmonHint="cortes",
        customPieceRiceHint="gramos", customPieceSalmonLabel="Pescado", historyKcalLabel="%d kcal",
        historyRiceLabel="%dg", historySalmonLabel="%d cortes", historySalmonLabelSingular="%d corte", statsWholeSalmon="Aprox. %.1f pescados enteros (%d cortes)",
        statsSalmonPieces="%d cortes de pescado", statsSalmonPiecesSingular="%d corte de pescado", shareActionTitle="Compartir sesión", shareIntentText="🍣 ¡Mi sesión de Sushi en %s! #SushiLog",
        shareWatermark="#SushiLog", rice="Arroz", kcal="Kcal", explore="EXPLORA",
        catTrajectory="Trayectoria", catAccumulation="Acumulación", catFeats="Hazañas", catSpecialist="Especialista", catExplorer="Explorador",
        duplicatePieceName="Ya existe una pieza con ese nombre",
        contact="Contacto"
    )

    private val english = Strings(
        back="Back", save="Save", exit="Exit", cancel="Cancel",
        delete="Delete", share="Share", confirm="Confirm", add="Add", edit="Edit", name="Name",
        begin="START", history="HISTORY", stats="STATISTICS", achievements="ACHIEVEMENTS", settings="Settings",
        darkTheme="Dark", lightTheme="Light", salmonTheme="Salmon",
        tagline="Sushi time!",
        newSession="New session", whereAreYouEating="Where are you eating?", restaurantName="Restaurant name...",
        start="Start", endSession="END SESSION", finishSession="End session?",
        exitDialogTitle="Exit session?", exitDialogMessage="If you leave now, you will lose your current progress.",
        totalPieces="pieces total", continueStr="Continue", noName="No name", total="Total",
        historyTitle="History", noHistory="No history", noHistoryDesc="Complete your first session\nto see your history here",
        sessions="sessions", session="session", pieces="pieces", piece="piece",
        deleteSession="Delete session?", deleteSessionConfirm="Session with %d pieces will be deleted.",
        today="Today", yesterday="Yesterday", daysAgo="%d days ago", weeksAgo="%d weeks ago", monthsAgo="%d months ago", others="Others",
        statsTitle="Statistics", noData="No data", noDataDesc="Complete sessions to\nsee your statistics",
        totalPiecesLabel="total pieces", totalPiecesLabelSingular="total piece", sessionCount="Sessions", average="Average", record="Record",
        breakdown="Breakdown by type", curiosities="Fun facts",
        riceApprox="You've eaten approx. %dg of rice", favoritePiece="Your favourite piece is: %s", salmonApprox="Approximately %d whole salmons",
        caloriesApprox="You've consumed about %d kcal",
        achievementsTitle="Achievements", achievementsUnlocked="achievements completed", unlocked="Unlocked",
        achTotal100Title="Beginner", achTotal500Title="Enthusiast", achTotal1000Title="Expert", achTotal5000Title="Sushi Master",
        achSession30Title="Good appetite", achSession50Title="Ravenous", achSession100Title="Eating machine",
        achNigiri100Title="Nigiri Fan", achSashimi100Title="Sashimi Lover", achMaki100Title="Maki Crazy", achGyoza50Title="Gyoza Lover",
        achNigiriSession30Title="Nigiri King", achSashimiSession20Title="Sashimi Master",
        achSessions5Title="Regular", achSessions25Title="VIP Customer", achSessions50Title="Buffet Legend",
        achVariety6Title="Explorer", achVarietyAllTitle="Full Taster", achAllInOneTitle="The Completionist", achFirstSessionTitle="First Time",
        achTotal100Desc="Eat 100 pieces in total", achTotal500Desc="Eat 500 pieces in total",
        achTotal1000Desc="Eat 1000 pieces in total", achTotal5000Desc="Eat 5000 pieces in total",
        achSession30Desc="Eat 30 pieces in one session", achSession50Desc="Eat 50 pieces in one session", achSession100Desc="Eat 100 pieces in one session",
        achNigiri100Desc="Eat 100 nigiris in total", achSashimi100Desc="Eat 100 sashimis in total",
        achMaki100Desc="Eat 100 makis in total", achGyoza50Desc="Eat 50 gyozas in total",
        achNigiriSession30Desc="Eat 30 nigiris in one session", achSashimiSession20Desc="Eat 20 sashimis in one session",
        achSessions5Desc="Complete 5 sessions", achSessions25Desc="Complete 25 sessions", achSessions50Desc="Complete 50 sessions",
        achVariety6Desc="Try 6 different types of pieces", achVarietyAllDesc="Try all types of pieces",
        achAllInOneDesc="Eat at least 1 of each type in one session", achFirstSessionDesc="Complete your first session",
        settingsTitle="Settings", appearance="Appearance", appTheme="App theme", language="Language",
        deleteAll="Delete all data", deleteAllSubtitle="Remove all your history",
        information="Information", version="Version", developedBy="Developed by",
        deleteAllConfirmTitle="Delete all data?",
        deleteAllConfirmMsg="This action cannot be undone. All your sessions, statistics and achievements will be deleted.",
        deleteAllConfirmBtn="Delete all", dataDeleted="Data deleted",
        customPieces="Custom pieces", customPiecesSubtitle="Add your own sushi types",
        addCustomPiece="Add piece", customPieceName="Piece name", customPieceNameHint="E.g: Onigiri, Futomaki...",
        noPieceName="Name cannot be empty", deleteCustomPiece="Delete piece?",
        deleteCustomPieceConfirm="\"%s\" will be removed from your custom pieces.",
        customPiecesManage="Manage custom pieces", customPiecesEmpty="You have no custom pieces yet",
        customPiecesLimit="Maximum limit of 12 custom pieces reached",
        deletedPiece="Deleted piece", customPieceKcalHint="approx.", customPieceSalmonHint="cuts",
        customPieceRiceHint="grams", customPieceSalmonLabel="Fish", historyKcalLabel="%d kcal",
        historyRiceLabel="%dg", historySalmonLabel="%d cuts", historySalmonLabelSingular="%d cut", statsWholeSalmon="Approx. %.1f whole fish (%d cuts)",
        statsSalmonPieces="%d fish cuts", statsSalmonPiecesSingular="%d fish cut", shareActionTitle="Share session", shareIntentText="🍣 My Sushi session at %s! #SushiLog",
        shareWatermark="#SushiLog", rice="Rice", kcal="Kcal", explore="EXPLORE",
        catTrajectory="Trajectory", catAccumulation="Accumulation", catFeats="Feats", catSpecialist="Specialist", catExplorer="Explorer",
        duplicatePieceName="A piece with this name already exists",
        contact="Contact"
    )

    private val french = Strings(
        back="Retour", save="Sauvegarder", exit="Sortir", cancel="Annuler",
        delete="Supprimer", share="Partager", confirm="Confirmer", add="Ajouter", edit="Modifier", name="Nom",
        begin="COMMENCER", history="HISTORIQUE", stats="STATISTIQUES", achievements="SUCCÈS", settings="Paramètres",
        darkTheme="Sombre", lightTheme="Clair", salmonTheme="Saumon",
        tagline="L'heure des sushis",
        newSession="Nouvelle session", whereAreYouEating="Où mangez-vous?", restaurantName="Nom du restaurant...",
        start="Démarrer", endSession="TERMINER LA SESSION", finishSession="Terminer la session?",
        exitDialogTitle="Quitter la session?", exitDialogMessage="Si vous partez maintenant, vous perdrez votre progression actuelle.",
        totalPieces="pièces au total", continueStr="Continuer", noName="Sans nom", total="Total",
        historyTitle="Historique", noHistory="Pas d'historique", noHistoryDesc="Terminez votre première session\npour voir votre historique ici",
        sessions="sessions", session="session", pieces="pièces", piece="pièce",
        deleteSession="Supprimer la session?", deleteSessionConfirm="La session avec %d pièces sera supprimée.",
        today="Aujourd'hui", yesterday="Hier", daysAgo="Il y a %d jours", weeksAgo="Il y a %d semaines", monthsAgo="Il y a %d mois", others="Autres",
        statsTitle="Statistiques", noData="Pas de données", noDataDesc="Terminez des sessions pour\nvoir vos statistiques",
        totalPiecesLabel="pièces au total", totalPiecesLabelSingular="pièce au total", sessionCount="Sessions", average="Moyenne", record="Record",
        breakdown="Répartition par type", curiosities="Anecdotes",
        riceApprox="Vous avez mangé environ %dg de riz", favoritePiece="Votre pièce préférée est : %s", salmonApprox="Environ %d saumons entiers",
        caloriesApprox="Vous avez consommé environ %d kcal",
        achievementsTitle="Succès", achievementsUnlocked="succès complétés", unlocked="Débloqué",
        achTotal100Title="Débutant", achTotal500Title="Passionné", achTotal1000Title="Expert", achTotal5000Title="Maître Sushi",
        achSession30Title="Bon appétit", achSession50Title="Grand appétit", achSession100Title="Machine à manger",
        achNigiri100Title="Fan de Nigiri", achSashimi100Title="Amateur de Sashimi", achMaki100Title="Fou du Maki", achGyoza50Title="Gyoza Lover",
        achNigiriSession30Title="Roi du Nigiri", achSashimiSession20Title="Maître Sashimi",
        achSessions5Title="Habitué", achSessions25Title="Client VIP", achSessions50Title="Légende du buffet",
        achVariety6Title="Explorateur", achVarietyAllTitle="Grand dégustateur", achAllInOneTitle="Le complétiste", achFirstSessionTitle="Première fois",
        achTotal100Desc="Mangez 100 pièces au total", achTotal500Desc="Mangez 500 pièces au total",
        achTotal1000Desc="Mangez 1000 pièces au total", achTotal5000Desc="Mangez 5000 pièces au total",
        achSession30Desc="Mangez 30 pièces en une session", achSession50Desc="Mangez 50 pièces en une session", achSession100Desc="Mangez 100 pièces en une session",
        achNigiri100Desc="Mangez 100 nigiris au total", achSashimi100Desc="Mangez 100 sashimis au total",
        achMaki100Desc="Mangez 100 makis au total", achGyoza50Desc="Mangez 50 gyozas au total",
        achNigiriSession30Desc="Mangez 30 nigiris en une session", achSashimiSession20Desc="Mangez 20 sashimis en une session",
        achSessions5Desc="Complétez 5 sessions", achSessions25Desc="Complétez 25 sessions", achSessions50Desc="Complétez 50 sessions",
        achVariety6Desc="Essayez 6 types différents de pièces", achVarietyAllDesc="Essayez tous les types de pièces",
        achAllInOneDesc="Mangez au moins 1 de chaque type en une session", achFirstSessionDesc="Complétez votre première session",
        settingsTitle="Paramètres", appearance="Apparence", appTheme="Thème de l'app", language="Langue",
        deleteAll="Supprimer toutes les données", deleteAllSubtitle="Supprimer tout votre historique",
        information="Informations", version="Version", developedBy="Développé par",
        deleteAllConfirmTitle="Supprimer toutes les données?",
        deleteAllConfirmMsg="Cette action est irréversible. Toutes vos sessions, statistiques et succès seront supprimés.",
        deleteAllConfirmBtn="Tout supprimer", dataDeleted="Données supprimées",
        customPieces="Pièces personnalisées", customPiecesSubtitle="Ajoutez vos propres types de sushi",
        addCustomPiece="Ajouter une pièce", customPieceName="Nom de la pièce", customPieceNameHint="Ex: Onigiri, Futomaki...",
        noPieceName="Le nom ne peut pas être vide", deleteCustomPiece="Supprimer la pièce?",
        deleteCustomPieceConfirm="\"%s\" sera supprimé de vos pièces personnalisées.",
        customPiecesManage="Gérer les pièces personnalisées", customPiecesEmpty="Vous n'avez pas encore de pièces personnalisées",
        customPiecesLimit="Limite de 12 pièces personnalisées atteinte",
        deletedPiece="Pièce supprimée", customPieceKcalHint="env.", customPieceSalmonHint="coupes",
        customPieceRiceHint="grammes", customPieceSalmonLabel="Poisson", historyKcalLabel="%d kcal",
        historyRiceLabel="%dg", historySalmonLabel="%d tranches", historySalmonLabelSingular="%d tranche", statsWholeSalmon="Env. %.1f poissons entiers (%d coupes)",
        statsSalmonPieces="%d coupes de poisson", statsSalmonPiecesSingular="%d coupe de poisson", shareActionTitle="Partager la session", shareIntentText="🍣 Ma session de Sushi chez %s! #SushiLog",
        shareWatermark="#SushiLog", rice="Riz", kcal="Kcal", explore="EXPLORER",
        catTrajectory="Parcours", catAccumulation="Accumulation", catFeats="Exploits", catSpecialist="Spécialiste", catExplorer="Explorateur",
        duplicatePieceName="Une pièce avec ce nom existe déjà",
        contact="Contact"
    )

    private val italian = Strings(
        back="Indietro", save="Salva", exit="Uscire", cancel="Annulla",
        delete="Elimina", share="Condividi", confirm="Conferma", add="Aggiungi", edit="Modifica", name="Nome",
        begin="INIZIA", history="STORICO", stats="STATISTICHE", achievements="OBIETTIVI", settings="Impostazioni",
        darkTheme="Scuro", lightTheme="Chiaro", salmonTheme="Salmone",
        tagline="Ora del sushi",
        newSession="Nuova sessione", whereAreYouEating="Dove stai mangiando?", restaurantName="Nome del ristorante...",
        start="Inizia", endSession="TERMINA SESSIONE", finishSession="Terminare la sessione?",
        exitDialogTitle="Uscire dalla sessione?", exitDialogMessage="Se esci ora, perderai i progressi attuali.",
        totalPieces="pezzi in totale", continueStr="Continua", noName="Senza nome", total="Totale",
        historyTitle="Storico", noHistory="Nessuno storico", noHistoryDesc="Completa la tua prima sessione\nper vedere il tuo storico qui",
        sessions="sessioni", session="sessione", pieces="pezzi", piece="pezzo",
        deleteSession="Eliminare la sessione?", deleteSessionConfirm="La sessione con %d pezzi verrà eliminata.",
        today="Oggi", yesterday="Ieri", daysAgo="%d giorni fa", weeksAgo="%d settimane fa", monthsAgo="%d mesi fa", others="Altro",
        statsTitle="Statistiche", noData="Nessun dato", noDataDesc="Completa le sessioni per\nvedere le tue statistiche",
        totalPiecesLabel="pezzi totali", totalPiecesLabelSingular="pezzo totale", sessionCount="Sessioni", average="Media", record="Record",
        breakdown="Ripartizione per tipo", curiosities="Curiosità",
        riceApprox="Hai mangiato circa %dg di riso", favoritePiece="Il tuo pezzo preferito è: %s", salmonApprox="Circa %d salmoni interi",
        caloriesApprox="Hai consumato circa %d kcal",
        achievementsTitle="Obiettivi", achievementsUnlocked="obiettivi completati", unlocked="Sbloccato",
        achTotal100Title="Principiante", achTotal500Title="Appassionato", achTotal1000Title="Esperto", achTotal5000Title="Maestro del Sushi",
        achSession30Title="Buon appetito", achSession50Title="Grande fame", achSession100Title="Macchina da mangiare",
        achNigiri100Title="Fan del Nigiri", achSashimi100Title="Amante del Sashimi", achMaki100Title="Matto per il Maki", achGyoza50Title="Gyoza Lover",
        achNigiriSession30Title="Re del Nigiri", achSashimiSession20Title="Maestro Sashimi",
        achSessions5Title="Abitudine", achSessions25Title="Cliente VIP", achSessions50Title="Leggenda del buffet",
        achVariety6Title="Esploratore", achVarietyAllTitle="Degustatore completo", achAllInOneTitle="Il completista", achFirstSessionTitle="Prima volta",
        achTotal100Desc="Mangia 100 pezzi in totale", achTotal500Desc="Mangia 500 pezzi in totale",
        achTotal1000Desc="Mangia 1000 pezzi in totale", achTotal5000Desc="Mangia 5000 pezzi in totale",
        achSession30Desc="Mangia 30 pezzi in una sessione", achSession50Desc="Mangia 50 pezzi in una sessione", achSession100Desc="Mangia 100 pezzi in una sessione",
        achNigiri100Desc="Mangia 100 nigiri in totale", achSashimi100Desc="Mangia 100 sashimi in totale",
        achMaki100Desc="Mangia 100 maki in totale", achGyoza50Desc="Mangia 50 gyoza in totale",
        achNigiriSession30Desc="Mangia 30 nigiri in una sessione", achSashimiSession20Desc="Mangia 20 sashimi in una sessione",
        achSessions5Desc="Completa 5 sessioni", achSessions25Desc="Completa 25 sessioni", achSessions50Desc="Completa 50 sessioni",
        achVariety6Desc="Prova 6 tipi diversi di pezzi", achVarietyAllDesc="Prova tutti i tipi di pezzi",
        achAllInOneDesc="Mangia almeno 1 di ogni tipo in una sessione", achFirstSessionDesc="Completa la tua prima sessione",
        settingsTitle="Impostazioni", appearance="Aspetto", appTheme="Tema dell'app", language="Lingua",
        deleteAll="Elimina tutti i dati", deleteAllSubtitle="Rimuovi tutto il tuo storico",
        information="Informazioni", version="Versione", developedBy="Sviluppato da",
        deleteAllConfirmTitle="Eliminare tutti i dati?",
        deleteAllConfirmMsg="Questa azione è irreversibile. Tutte le tue sessioni, statistiche e obiettivi saranno eliminati.",
        deleteAllConfirmBtn="Elimina tutto", dataDeleted="Dati eliminati",
        customPieces="Pezzi personalizzati", customPiecesSubtitle="Aggiungi i tuoi tipi di sushi",
        addCustomPiece="Aggiungi pezzo", customPieceName="Nome del pezzo", customPieceNameHint="Es: Onigiri, Futomaki...",
        noPieceName="Il nome non può essere vuoto", deleteCustomPiece="Eliminare il pezzo?",
        deleteCustomPieceConfirm="\"%s\" verrà rimosso dai tuoi pezzi personalizzati.",
        customPiecesManage="Gestisci pezzi personalizzati", customPiecesEmpty="Non hai ancora pezzi personalizzati",
        customPiecesLimit="Limite di 12 pezzi personalizzati raggiunto",
        deletedPiece="Pezzo eliminato", customPieceKcalHint="circa", customPieceSalmonHint="tagli",
        customPieceRiceHint="grammi", customPieceSalmonLabel="Pesce", historyKcalLabel="%d kcal",
        historyRiceLabel="%dg", historySalmonLabel="%d tagli", historySalmonLabelSingular="%d taglio", statsWholeSalmon="Circa %.1f pesci interi (%d tagli)",
        statsSalmonPieces="%d tagli di pesce", statsSalmonPiecesSingular="%d taglio di pesce", shareActionTitle="Condividi sessione", shareIntentText="🍣 La mia sessione di Sushi al %s! #SushiLog",
        shareWatermark="#SushiLog", rice="Riso", kcal="Kcal", explore="ESPLORA",
        catTrajectory="Traiettoria", catAccumulation="Accumulo", catFeats="Imprese", catSpecialist="Specialista", catExplorer="Esploratore",
        duplicatePieceName="Esiste già un pezzo con questo nome",
        contact="Contatto"
    )

    fun get(language: AppLanguage): Strings = when (language) {
        AppLanguage.SPANISH -> spanish
        AppLanguage.ENGLISH -> english
        AppLanguage.FRENCH  -> french
        AppLanguage.ITALIAN -> italian
    }

    fun getAchievementTitle(id: String, strings: Strings): String = when (id) {
        "total_100" -> strings.achTotal100Title; "total_500" -> strings.achTotal500Title
        "total_1000" -> strings.achTotal1000Title; "total_5000" -> strings.achTotal5000Title
        "session_30" -> strings.achSession30Title; "session_50" -> strings.achSession50Title
        "session_100" -> strings.achSession100Title
        "nigiri_100" -> strings.achNigiri100Title; "sashimi_100" -> strings.achSashimi100Title
        "maki_100" -> strings.achMaki100Title; "gyoza_50" -> strings.achGyoza50Title
        "nigiri_session_30" -> strings.achNigiriSession30Title
        "sashimi_session_20" -> strings.achSashimiSession20Title
        "sessions_5" -> strings.achSessions5Title; "sessions_25" -> strings.achSessions25Title
        "sessions_50" -> strings.achSessions50Title
        "variety_6" -> strings.achVariety6Title; "variety_all" -> strings.achVarietyAllTitle
        "all_in_one" -> strings.achAllInOneTitle; "first_session" -> strings.achFirstSessionTitle
        else -> id
    }

    fun getAchievementDescription(id: String, strings: Strings): String = when (id) {
        "total_100" -> strings.achTotal100Desc; "total_500" -> strings.achTotal500Desc
        "total_1000" -> strings.achTotal1000Desc; "total_5000" -> strings.achTotal5000Desc
        "session_30" -> strings.achSession30Desc; "session_50" -> strings.achSession50Desc
        "session_100" -> strings.achSession100Desc
        "nigiri_100" -> strings.achNigiri100Desc; "sashimi_100" -> strings.achSashimi100Desc
        "maki_100" -> strings.achMaki100Desc; "gyoza_50" -> strings.achGyoza50Desc
        "nigiri_session_30" -> strings.achNigiriSession30Desc
        "sashimi_session_20" -> strings.achSashimiSession20Desc
        "sessions_5" -> strings.achSessions5Desc; "sessions_25" -> strings.achSessions25Desc
        "sessions_50" -> strings.achSessions50Desc
        "variety_6" -> strings.achVariety6Desc; "variety_all" -> strings.achVarietyAllDesc
        "all_in_one" -> strings.achAllInOneDesc; "first_session" -> strings.achFirstSessionDesc
        else -> ""
    }
}

/** Pieza de sushi personalizada creada por el usuario. */
data class CustomPiece(
    val id: String,
    val name: String,
    val iconId: Int = pls.dev.sushilog.R.drawable.nigiri,
    val kcal: Int = 0,
    val salmonCount: Int = 0,
    val riceGrams: Int = 0
)

/**
 * Gestiona preferencias del usuario: tema, idioma y piezas personalizadas.
 * Máximo 12 piezas personalizadas.
 */
class AppSettingsManager(private val context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("sushi_app_settings", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun getTheme(): AppTheme {
        if (!prefs.contains("theme")) {
            val isDark = (context.resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK) == android.content.res.Configuration.UI_MODE_NIGHT_YES
            val defaultTheme = if (isDark) AppTheme.DARK else AppTheme.LIGHT
            setTheme(defaultTheme)
            return defaultTheme
        }
        val themeId = prefs.getString("theme", AppTheme.LIGHT.id) ?: AppTheme.LIGHT.id
        return AppTheme.entries.find { it.id == themeId } ?: AppTheme.LIGHT
    }

    fun setTheme(theme: AppTheme) {
        prefs.edit { putString("theme", theme.id) }
    }

    fun getLanguage(): AppLanguage {
        if (!prefs.contains("language")) {
            val detected = AppLanguage.fromDeviceLocale()
            setLanguage(detected)
            return detected
        }
        val code = prefs.getString("language", AppLanguage.ENGLISH.code) ?: AppLanguage.ENGLISH.code
        return AppLanguage.entries.find { it.code == code } ?: AppLanguage.ENGLISH
    }

    fun setLanguage(language: AppLanguage) {
        prefs.edit { putString("language", language.code) }
    }



    fun getCustomPieces(): List<CustomPiece> {
        val json = prefs.getString("custom_pieces", null) ?: return emptyList()
        return try {
            val type = object : TypeToken<List<CustomPiece>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun saveCustomPieces(pieces: List<CustomPiece>) {
        prefs.edit(commit = true) { putString("custom_pieces", gson.toJson(pieces)) }
    }

    fun addCustomPiece(piece: CustomPiece) {
        val current = getCustomPieces().toMutableList()
        current.add(piece)
        saveCustomPieces(current)
    }

    fun removeCustomPiece(pieceId: String) {
        val current = getCustomPieces().toMutableList()
        current.removeAll { it.id == pieceId }
        saveCustomPieces(current)
    }

    fun updateCustomPiece(updated: CustomPiece) {
        val current = getCustomPieces().toMutableList()
        val index = current.indexOfFirst { it.id == updated.id }
        if (index != -1) {
            current[index] = updated
            saveCustomPieces(current)
        }
    }
}