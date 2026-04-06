package org.example.project.travel.frontEnd.Screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travel.network.BASE_URL
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.example.project.travel.frontend.model.DestinationCity
import kotlinx.coroutines.launch
import java.net.URLEncoder
import io.ktor.client.statement.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

@Composable
fun StateScreen(
    stateName: String,
    onCitySelected: (cityId: String, cityName: String) -> Unit
) {
    val topCities = remember(stateName) {
        when (stateName) {
            "Andhra Pradesh" -> listOf(
                DestinationCity(11, "Visakhapatnam", "Andhra Pradesh", "India", 301),
                DestinationCity(12, "Vijayawada", "Andhra Pradesh", "India", 302),
                DestinationCity(13, "Tirupati", "Andhra Pradesh", "India", 303),
                DestinationCity(14, "Amaravati", "Andhra Pradesh", "India", 304),
                DestinationCity(15, "Kurnool", "Andhra Pradesh", "India", 305),
                DestinationCity(16, "Nellore", "Andhra Pradesh", "India", 306),
                DestinationCity(17, "Rajahmundry", "Andhra Pradesh", "India", 307),
                DestinationCity(18, "Kakinada", "Andhra Pradesh", "India", 308),
                DestinationCity(19, "Chittoor", "Andhra Pradesh", "India", 309),
                DestinationCity(20, "Anantapur", "Andhra Pradesh", "India", 310)
            )
            "Arunachal Pradesh" -> listOf(
                DestinationCity(21, "Itanagar", "Arunachal Pradesh", "India", 401),
                DestinationCity(22, "Tawang", "Arunachal Pradesh", "India", 402),
                DestinationCity(23, "Ziro", "Arunachal Pradesh", "India", 403),
                DestinationCity(24, "Bomdila", "Arunachal Pradesh", "India", 404),
                DestinationCity(25, "Pasighat", "Arunachal Pradesh", "India", 405),
                DestinationCity(26, "Roing", "Arunachal Pradesh", "India", 406),
                DestinationCity(27, "Aalo", "Arunachal Pradesh", "India", 407),
                DestinationCity(28, "Dirang", "Arunachal Pradesh", "India", 408),
                DestinationCity(29, "Changlang", "Arunachal Pradesh", "India", 409),
                DestinationCity(30, "Khonsa", "Arunachal Pradesh", "India", 410)
            )
            "Assam" -> listOf(
                DestinationCity(31, "Guwahati", "Assam", "India", 501),
                DestinationCity(32, "Dibrugarh", "Assam", "India", 502),
                DestinationCity(33, "Jorhat", "Assam", "India", 503),
                DestinationCity(34, "Silchar", "Assam", "India", 504),
                DestinationCity(35, "Tezpur", "Assam", "India", 505),
                DestinationCity(36, "Sivasagar", "Assam", "India", 506),
                DestinationCity(37, "Tinsukia", "Assam", "India", 507),
                DestinationCity(38, "Haflong", "Assam", "India", 508),
                DestinationCity(39, "Barpeta", "Assam", "India", 509),
                DestinationCity(40, "Goalpara", "Assam", "India", 510)
            )
            "Bihar" -> listOf(
                DestinationCity(41, "Patna", "Bihar", "India", 101),
                DestinationCity(42, "Gaya", "Bihar", "India", 102),
                DestinationCity(43, "Bodh Gaya", "Bihar", "India", 103),
                DestinationCity(44, "Nalanda", "Bihar", "India", 104),
                DestinationCity(45, "Rajgir", "Bihar", "India", 105),
                DestinationCity(46, "Vaishali", "Bihar", "India", 106),
                DestinationCity(47, "Muzaffarpur", "Bihar", "India", 107),
                DestinationCity(48, "Bhagalpur", "Bihar", "India", 108),
                DestinationCity(49, "Pawapuri", "Bihar", "India", 109),
                DestinationCity(50, "Sasaram", "Bihar", "India", 110)
            )
            "Chhattisgarh" -> listOf(
                DestinationCity(51, "Raipur", "Chhattisgarh", "India", 601),
                DestinationCity(52, "Bilaspur", "Chhattisgarh", "India", 602),
                DestinationCity(53, "Jagdalpur", "Chhattisgarh", "India", 603),
                DestinationCity(54, "Durg", "Chhattisgarh", "India", 604),
                DestinationCity(55, "Bhilai", "Chhattisgarh", "India", 605),
                DestinationCity(56, "Rajnandgaon", "Chhattisgarh", "India", 606),
                DestinationCity(57, "Korba", "Chhattisgarh", "India", 607),
                DestinationCity(58, "Bhatapara", "Chhattisgarh", "India", 608),
                DestinationCity(59, "Bilaspine", "Chhattisgarh", "India", 609),
                DestinationCity(60, "Dhamtari", "Chhattisgarh", "India", 610)
            )
            "Goa" -> listOf(
                DestinationCity(61, "Panjim", "Goa", "India", 701),
                DestinationCity(62, "Margao", "Goa", "India", 702),
                DestinationCity(63, "Vasco da Gama", "Goa", "India", 703),
                DestinationCity(64, "Ponda", "Goa", "India", 704),
                DestinationCity(65, "Mormugao", "Goa", "India", 705),
                DestinationCity(66, "Sanguem", "Goa", "India", 706),
                DestinationCity(67, "Mukta", "Goa", "India", 707),
                DestinationCity(68, "Quepem", "Goa", "India", 708),
                DestinationCity(69, "Canacona", "Goa", "India", 709),
                DestinationCity(70, "Siddapur", "Goa", "India", 710)
            )
            "Gujarat" -> listOf(
                DestinationCity(71, "Ahmedabad", "Gujarat", "India", 801),
                DestinationCity(72, "Surat", "Gujarat", "India", 802),
                DestinationCity(73, "Vadodara", "Gujarat", "India", 803),
                DestinationCity(74, "Rajkot", "Gujarat", "India", 804),
                DestinationCity(75, "Bhavnagar", "Gujarat", "India", 805),
                DestinationCity(76, "Jamnagar", "Gujarat", "India", 806),
                DestinationCity(77, "Gandhinagar", "Gujarat", "India", 807),
                DestinationCity(78, "Bharuch", "Gujarat", "India", 808),
                DestinationCity(79, "Vapi", "Gujarat", "India", 809),
                DestinationCity(80, "Anand", "Gujarat", "India", 810)
            )
            "Haryana" -> listOf(
                DestinationCity(81, "Gurgaon", "Haryana", "India", 901),
                DestinationCity(82, "Faridabad", "Haryana", "India", 902),
                DestinationCity(83, "Panipat", "Haryana", "India", 903),
                DestinationCity(84, "Sonipat", "Haryana", "India", 904),
                DestinationCity(85, "Rohtak", "Haryana", "India", 905),
                DestinationCity(86, "Jind", "Haryana", "India", 906),
                DestinationCity(87, "Bhiwani", "Haryana", "India", 907),
                DestinationCity(88, "Yamunanagar", "Haryana", "India", 908),
                DestinationCity(89, "Panchkula", "Haryana", "India", 909),
                DestinationCity(90, "Karnal", "Haryana", "India", 910)
            )
            "Himachal Pradesh" -> listOf(
                DestinationCity(91, "Shimla", "Himachal Pradesh", "India", 1001),
                DestinationCity(92, "Manali", "Himachal Pradesh", "India", 1002),
                DestinationCity(93, "Dharamshala", "Himachal Pradesh", "India", 1003),
                DestinationCity(94, "Mcleodganj", "Himachal Pradesh", "India", 1004),
                DestinationCity(95, "Kullu", "Himachal Pradesh", "India", 1005),
                DestinationCity(96, "Kangra", "Himachal Pradesh", "India", 1006),
                DestinationCity(97, "Mandi", "Himachal Pradesh", "India", 1007),
                DestinationCity(98, "Nahan", "Himachal Pradesh", "India", 1008),
                DestinationCity(99, "Solan", "Himachal Pradesh", "India", 1009),
                DestinationCity(100, "Palampur", "Himachal Pradesh", "India", 1010)
            )
            "Jharkhand" -> listOf(
                DestinationCity(101, "Ranchi", "Jharkhand", "India", 1101),
                DestinationCity(102, "Jamshedpur", "Jharkhand", "India", 1102),
                DestinationCity(103, "Dhanbad", "Jharkhand", "India", 1103),
                DestinationCity(104, "Bokaro Steel City", "Jharkhand", "India", 1104),
                DestinationCity(105, "Deoghar", "Jharkhand", "India", 1105),
                DestinationCity(106, "Hazaribag", "Jharkhand", "India", 1106),
                DestinationCity(107, "Giridih", "Jharkhand", "India", 1107),
                DestinationCity(108, "Dumka", "Jharkhand", "India", 1108),
                DestinationCity(109, "Pakaur", "Jharkhand", "India", 1109),
                DestinationCity(110, "Chaibasa", "Jharkhand", "India", 1110)
            )
            "Karnataka" -> listOf(
                DestinationCity(111, "Bengaluru", "Karnataka", "India", 1201),
                DestinationCity(112, "Mysuru", "Karnataka", "India", 1202),
                DestinationCity(113, "Mangalore", "Karnataka", "India", 1203),
                DestinationCity(114, "Hubli", "Karnataka", "India", 1204),
                DestinationCity(115, "Belgaum", "Karnataka", "India", 1205),
                DestinationCity(116, "Dharwad", "Karnataka", "India", 1206),
                DestinationCity(117, "Bijapur", "Karnataka", "India", 1207),
                DestinationCity(118, "Gulbarga", "Karnataka", "India", 1208),
                DestinationCity(119, "Bellary", "Karnataka", "India", 1209),
                DestinationCity(120, "Tumkur", "Karnataka", "India", 1210)
            )
            "Kerala" -> listOf(
                DestinationCity(121, "Kochi", "Kerala", "India", 1301),
                DestinationCity(122, "Thiruvananthapuram", "Kerala", "India", 1302),
                DestinationCity(123, "Kozhikode", "Kerala", "India", 1303),
                DestinationCity(124, "Kollam", "Kerala", "India", 1304),
                DestinationCity(125, "Alappuzha", "Kerala", "India", 1305),
                DestinationCity(126, "Kannur", "Kerala", "India", 1306),
                DestinationCity(127, "Palakkad", "Kerala", "India", 1307),
                DestinationCity(128, "Thrissur", "Kerala", "India", 1308),
                DestinationCity(129, "Malappuram", "Kerala", "India", 1309),
                DestinationCity(130, "Wayanad", "Kerala", "India", 1310)
            )
            "Madhya Pradesh" -> listOf(
                DestinationCity(131, "Bhopal", "Madhya Pradesh", "India", 201),
                DestinationCity(132, "Indore", "Madhya Pradesh", "India", 202),
                DestinationCity(133, "Gwalior", "Madhya Pradesh", "India", 203),
                DestinationCity(134, "Jabalpur", "Madhya Pradesh", "India", 204),
                DestinationCity(135, "Ujjain", "Madhya Pradesh", "India", 205),
                DestinationCity(136, "Bhopal", "Madhya Pradesh", "India", 206),
                DestinationCity(137, "Gwalior", "Madhya Pradesh", "India", 207),
                DestinationCity(138, "Jabalpur", "Madhya Pradesh", "India", 208),
                DestinationCity(139, "Maheshwar", "Madhya Pradesh", "India", 209),
                DestinationCity(140, "Bhopal", "Madhya Pradesh", "India", 210)
            )
            "Maharashtra" -> listOf(
                DestinationCity(141, "Mumbai", "Maharashtra", "India", 1401),
                DestinationCity(142, "Pune", "Maharashtra", "India", 1402),
                DestinationCity(143, "Nagpur", "Maharashtra", "India", 1403),
                DestinationCity(144, "Nashik", "Maharashtra", "India", 1404),
                DestinationCity(145, "Aurangabad", "Maharashtra", "India", 1405),
                DestinationCity(146, "Kolhapur", "Maharashtra", "India", 1406),
                DestinationCity(147, "Sangli", "Maharashtra", "India", 1407),
                DestinationCity(148, "Solapur", "Maharashtra", "India", 1408),
                DestinationCity(149, "Satara", "Maharashtra", "India", 1409),
                DestinationCity(150, "Pune", "Maharashtra", "India", 1410)
            )
            "Manipur" -> listOf(
                DestinationCity(151, "Imphal", "Manipur", "India", 1501),
                DestinationCity(152, "Thoubal", "Manipur", "India", 1502),
                DestinationCity(153, "Bishnupur", "Manipur", "India", 1503),
                DestinationCity(154, "Chandel", "Manipur", "India", 1504),
                DestinationCity(155, "Tamenglong", "Manipur", "India", 1505),
                DestinationCity(156, "Ukhrul", "Manipur", "India", 1506),
                DestinationCity(157, "Churachandpur", "Manipur", "India", 1507),
                DestinationCity(158, "Senapati", "Manipur", "India", 1508),
                DestinationCity(159, "Tamenglong", "Manipur", "India", 1509),
                DestinationCity(160, "Ukhrul", "Manipur", "India", 1510)
            )
            "Meghalaya" -> listOf(
                DestinationCity(161, "Shillong", "Meghalaya", "India", 1601),
                DestinationCity(162, "Cherrapunji", "Meghalaya", "India", 1602),
                DestinationCity(163, "Tura", "Meghalaya", "India", 1603),
                DestinationCity(164, "Nongpoh", "Meghalaya", "India", 1604),
                DestinationCity(165, "Mawlai", "Meghalaya", "India", 1605),
                DestinationCity(166, "Jowai", "Meghalaya", "India", 1606),
                DestinationCity(167, "Nongstoin", "Meghalaya", "India", 1607),
                DestinationCity(168, "Mawlai", "Meghalaya", "India", 1608),
                DestinationCity(169, "Nongpoh", "Meghalaya", "India", 1609),
                DestinationCity(170, "Mawlai", "Meghalaya", "India", 1610)
            )
            "Mizoram" -> listOf(
                DestinationCity(171, "Aizawl", "Mizoram", "India", 1701),
                DestinationCity(172, "Lunglei", "Mizoram", "India", 1702),
                DestinationCity(173, "Champhai", "Mizoram", "India", 1703),
                DestinationCity(174, "Saiha", "Mizoram", "India", 1704),
                DestinationCity(175, "Kolasib", "Mizoram", "India", 1705),
                DestinationCity(176, "Serchhip", "Mizoram", "India", 1706),
                DestinationCity(177, "Lunglei", "Mizoram", "India", 1707),
                DestinationCity(178, "Champhai", "Mizoram", "India", 1708),
                DestinationCity(179, "Saiha", "Mizoram", "India", 1709),
                DestinationCity(180, "Kolasib", "Mizoram", "India", 1710)
            )
            "Nagaland" -> listOf(
                DestinationCity(181, "Kohima", "Nagaland", "India", 1801),
                DestinationCity(182, "Dimapur", "Nagaland", "India", 1802),
                DestinationCity(183, "Mokokchung", "Nagaland", "India", 1803),
                DestinationCity(184, "Tuensang", "Nagaland", "India", 1804),
                DestinationCity(185, "Mon", "Nagaland", "India", 1805),
                DestinationCity(186, "Phek", "Nagaland", "India", 1806),
                DestinationCity(187, "Wokha", "Nagaland", "India", 1807),
                DestinationCity(188, "Zunheboto", "Nagaland", "India", 1808),
                DestinationCity(189, "Daporijo", "Nagaland", "India", 1809),
                DestinationCity(190, "Chumukedima", "Nagaland", "India", 1810)
            )
            "Odisha" -> listOf(
                DestinationCity(191, "Bhubaneswar", "Odisha", "India", 1901),
                DestinationCity(192, "Puri", "Odisha", "India", 1902),
                DestinationCity(193, "Cuttack", "Odisha", "India", 1903),
                DestinationCity(194, "Bhubaneswar", "Odisha", "India", 1904),
                DestinationCity(195, "Puri", "Odisha", "India", 1905),
                DestinationCity(196, "Cuttack", "Odisha", "India", 1906),
                DestinationCity(197, "Bhubaneswar", "Odisha", "India", 1907),
                DestinationCity(198, "Puri", "Odisha", "India", 1908),
                DestinationCity(199, "Cuttack", "Odisha", "India", 1909),
                DestinationCity(200, "Bhubaneswar", "Odisha", "India", 1910)
            )
            "Punjab" -> listOf(
                DestinationCity(201, "Amritsar", "Punjab", "India", 2001),
                DestinationCity(202, "Ludhiana", "Punjab", "India", 2002),
                DestinationCity(203, "Jalandhar", "Punjab", "India", 2003),
                DestinationCity(204, "Patiala", "Punjab", "India", 2004),
                DestinationCity(205, "Bathinda", "Punjab", "India", 2005),
                DestinationCity(206, "Moga", "Punjab", "India", 2006),
                DestinationCity(207, "Fatehgarh Sahib", "Punjab", "India", 2007),
                DestinationCity(208, "Sangrur", "Punjab", "India", 2008),
                DestinationCity(209, "Faridkot", "Punjab", "India", 2009),
                DestinationCity(210, "Muktsar", "Punjab", "India", 2010)
            )
            "Rajasthan" -> listOf(
                DestinationCity(211, "Jaipur", "Rajasthan", "India", 2101),
                DestinationCity(212, "Udaipur", "Rajasthan", "India", 2102),
                DestinationCity(213, "Jodhpur", "Rajasthan", "India", 2103),
                DestinationCity(214, "Bikaner", "Rajasthan", "India", 2104),
                DestinationCity(215, "Jaisalmer", "Rajasthan", "India", 2105),
                DestinationCity(216, "Udaipur", "Rajasthan", "India", 2106),
                DestinationCity(217, "Jodhpur", "Rajasthan", "India", 2107),
                DestinationCity(218, "Bikaner", "Rajasthan", "India", 2108),
                DestinationCity(219, "Jaisalmer", "Rajasthan", "India", 2109),
                DestinationCity(220, "Udaipur", "Rajasthan", "India", 2110)
            )
            "Sikkim" -> listOf(
                DestinationCity(221, "Gangtok", "Sikkim", "India", 2201),
                DestinationCity(222, "Pelling", "Sikkim", "India", 2202),
                DestinationCity(223, "Namchi", "Sikkim", "India", 2203),
                DestinationCity(224, "Gangtok", "Sikkim", "India", 2204),
                DestinationCity(225, "Pelling", "Sikkim", "India", 2205),
                DestinationCity(226, "Namchi", "Sikkim", "India", 2206),
                DestinationCity(227, "Gangtok", "Sikkim", "India", 2207),
                DestinationCity(228, "Pelling", "Sikkim", "India", 2208),
                DestinationCity(229, "Namchi", "Sikkim", "India", 2209),
                DestinationCity(230, "Gangtok", "Sikkim", "India", 2210)
            )
            "Tamil Nadu" -> listOf(
                DestinationCity(231, "Chennai", "Tamil Nadu", "India", 2301),
                DestinationCity(232, "Madurai", "Tamil Nadu", "India", 2302),
                DestinationCity(233, "Coimbatore", "Tamil Nadu", "India", 2303),
                DestinationCity(234, "Tiruchirappalli", "Tamil Nadu", "India", 2304),
                DestinationCity(235, "Salem", "Tamil Nadu", "India", 2305),
                DestinationCity(236, "Erode", "Tamil Nadu", "India", 2306),
                DestinationCity(237, "Thanjavur", "Tamil Nadu", "India", 2307),
                DestinationCity(238, "Tirunelveli", "Tamil Nadu", "India", 2308),
                DestinationCity(239, "Tuticorin", "Tamil Nadu", "India", 2309),
                DestinationCity(240, "Tiruvannamalai", "Tamil Nadu", "India", 2310)
            )
            "Telangana" -> listOf(
                DestinationCity(241, "Hyderabad", "Telangana", "India", 2401),
                DestinationCity(242, "Warangal", "Telangana", "India", 2402),
                DestinationCity(243, "Nizamabad", "Telangana", "India", 2403),
                DestinationCity(244, "Karimnagar", "Telangana", "India", 2404),
                DestinationCity(245, "Khammam", "Telangana", "India", 2405),
                DestinationCity(246, "Mahbubnagar", "Telangana", "India", 2406),
                DestinationCity(247, "Adilabad", "Telangana", "India", 2407),
                DestinationCity(248, "Nalgonda", "Telangana", "India", 2408),
                DestinationCity(249, "Suryapet", "Telangana", "India", 2409),
                DestinationCity(250, "Khammam", "Telangana", "India", 2410)
            )
            "Tripura" -> listOf(
                DestinationCity(251, "Agartala", "Tripura", "India", 2501),
                DestinationCity(252, "Udaipur", "Tripura", "India", 2502),
                DestinationCity(253, "Dharmanagar", "Tripura", "India", 2503),
                DestinationCity(254, "Kailashahar", "Tripura", "India", 2504),
                DestinationCity(255, "Uttar Kannada", "Tripura", "India", 2505),
                DestinationCity(256, "South Tripura", "Tripura", "India", 2506),
                DestinationCity(257, "North Tripura", "Tripura", "India", 2507),
                DestinationCity(258, "West Tripura", "Tripura", "India", 2508),
                DestinationCity(259, "East Tripura", "Tripura", "India", 2509),
                DestinationCity(260, "Udaipur", "Tripura", "India", 2510)
            )
            "Uttar Pradesh" -> listOf(
                DestinationCity(261, "Agra", "Uttar Pradesh", "India", 2601),
                DestinationCity(262, "Varanasi", "Uttar Pradesh", "India", 2602),
                DestinationCity(263, "Lucknow", "Uttar Pradesh", "India", 2603),
                DestinationCity(264, "Kanpur", "Uttar Pradesh", "India", 2604),
                DestinationCity(265, "Meerut", "Uttar Pradesh", "India", 2605),
                DestinationCity(266, "Gorakhpur", "Uttar Pradesh", "India", 2606),
                DestinationCity(267, "Jhansi", "Uttar Pradesh", "India", 2607),
                DestinationCity(268, "Bareilly", "Uttar Pradesh", "India", 2608),
                DestinationCity(269, "Aligarh", "Uttar Pradesh", "India", 2609),
                DestinationCity(270, "Agra", "Uttar Pradesh", "India", 2610)
            )
            "Uttarakhand" -> listOf(
                DestinationCity(271, "Dehradun", "Uttarakhand", "India", 2701),
                DestinationCity(272, "Haridwar", "Uttarakhand", "India", 2702),
                DestinationCity(273, "Nainital", "Uttarakhand", "India", 2703),
                DestinationCity(274, "Roorkee", "Uttarakhand", "India", 2704),
                DestinationCity(275, "Pithoragarh", "Uttarakhand", "India", 2705),
                DestinationCity(276, "Chamoli", "Uttarakhand", "India", 2706),
                DestinationCity(277, "Rudraprayag", "Uttarakhand", "India", 2707),
                DestinationCity(278, "Nainital", "Uttarakhand", "India", 2708),
                DestinationCity(279, "Roorkee", "Uttarakhand", "India", 2709),
                DestinationCity(280, "Dehradun", "Uttarakhand", "India", 2710)
            )
            "West Bengal" -> listOf(
                DestinationCity(281, "Kolkata", "West Bengal", "India", 2801),
                DestinationCity(282, "Darjeeling", "West Bengal", "India", 2802),
                DestinationCity(283, "Siliguri", "West Bengal", "India", 2803),
                DestinationCity(284, "Kolkata", "West Bengal", "India", 2804),
                DestinationCity(285, "Darjeeling", "West Bengal", "India", 2805),
                DestinationCity(286, "Siliguri", "West Bengal", "India", 2806),
                DestinationCity(287, "Kolkata", "West Bengal", "India", 2807),
                DestinationCity(288, "Darjeeling", "West Bengal", "India", 2808),
                DestinationCity(289, "Siliguri", "West Bengal", "India", 2809),
                DestinationCity(290, "Kolkata", "West Bengal", "India", 2810)
            )
            else -> emptyList()
        }
    }

    val httpClient = remember {
        HttpClient {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
    }

    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    // State-specific theme
    val stateTheme = getStateTheme(stateName)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = stateTheme.gradientColors
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Enhanced Header
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.95f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stateTheme.emoji,
                        fontSize = 40.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Top Tourist Cities in $stateName",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = stateTheme.primaryColor,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (topCities.isEmpty()) {
                // Enhanced Empty State
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.9f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🏛️",
                            fontSize = 48.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("No cities found for $stateName.")
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(topCities) { city ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(6.dp, RoundedCornerShape(16.dp))
                                .clickable {
                                    coroutineScope.launch {
                                        isLoading = true
                                        error = null
                                        try {
                                            val encodedCity = URLEncoder.encode(city.city, "UTF-8")
                                            val encodedState = URLEncoder.encode(city.state, "UTF-8")
                                            val url = "$BASE_URL/api/city-details?city=$encodedCity&state=$encodedState"
                                            val response: HttpResponse = httpClient.get(url)
                                            val raw = response.bodyAsText()
                                            println("Raw response: $raw")
                                            val result = response.body<DestinationCity>()
                                            onCitySelected(result.id.toString(), result.city)
                                        } catch (e: Exception) {
                                            error = "City not found or network error: ${e.message}"
                                        } finally {
                                            isLoading = false
                                        }
                                    }
                                },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Row(
                                modifier = Modifier.padding(20.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // City Icon
                                Box(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .background(
                                            brush = Brush.radialGradient(
                                                colors = listOf(
                                                    stateTheme.primaryColor,
                                                    stateTheme.secondaryColor
                                                )
                                            ),
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LocationCity,
                                        contentDescription = "City",
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                // City Name
                                Text(
                                    city.city,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Black,
                                    modifier = Modifier.weight(1f)
                                )

                                // Arrow Icon
                                Icon(
                                    Icons.Default.ArrowForward,
                                    contentDescription = "Go to city",
                                    tint = stateTheme.primaryColor,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Enhanced Loading State
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = stateTheme.primaryColor,
                            strokeWidth = 3.dp,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Loading city details...",
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }

        // Enhanced Error Display
        error?.let {
            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Red.copy(alpha = 0.9f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Error,
                        contentDescription = "Error",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = it,
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

// Helper data class and function
data class StateTheme(
    val primaryColor: Color,
    val secondaryColor: Color,
    val gradientColors: List<Color>,
    val emoji: String
)

private fun getStateTheme(stateName: String): StateTheme {
    return when (stateName) {
        "Rajasthan" -> StateTheme(
            primaryColor = Color(0xFFE91E63),
            secondaryColor = Color(0xFFFF5722),
            gradientColors = listOf(Color(0xFFE91E63), Color(0xFFFF5722), Color(0xFFFFC107)),
            emoji = "🏰"
        )
        "Kerala" -> StateTheme(
            primaryColor = Color(0xFF4CAF50),
            secondaryColor = Color(0xFF8BC34A),
            gradientColors = listOf(Color(0xFF4CAF50), Color(0xFF8BC34A), Color(0xFFCDDC39)),
            emoji = "🌴"
        )
        "Goa" -> StateTheme(
            primaryColor = Color(0xFF00BCD4),
            secondaryColor = Color(0xFF03DAC6),
            gradientColors = listOf(Color(0xFF00BCD4), Color(0xFF03DAC6), Color(0xFF81C784)),
            emoji = "🏖️"
        )
        "Himachal Pradesh" -> StateTheme(
            primaryColor = Color(0xFF2196F3),
            secondaryColor = Color(0xFF03DAC6),
            gradientColors = listOf(Color(0xFF2196F3), Color(0xFF03DAC6), Color(0xFFE1F5FE)),
            emoji = "🏔️"
        )
        "Maharashtra" -> StateTheme(
            primaryColor = Color(0xFFFF9800),
            secondaryColor = Color(0xFFFF5722),
            gradientColors = listOf(Color(0xFFFF9800), Color(0xFFFF5722), Color(0xFFFFC107)),
            emoji = "🏙️"
        )
        "Tamil Nadu" -> StateTheme(
            primaryColor = Color(0xFF9C27B0),
            secondaryColor = Color(0xFFE91E63),
            gradientColors = listOf(Color(0xFF9C27B0), Color(0xFFE91E63), Color(0xFFF8BBD9)),
            emoji = "🕌"
        )
        else -> StateTheme(
            primaryColor = Color(0xFF667eea),
            secondaryColor = Color(0xFF764ba2),
            gradientColors = listOf(Color(0xFF667eea), Color(0xFF764ba2), Color(0xFFF093FB)),
            emoji = "🏛️"
        )
    }
}