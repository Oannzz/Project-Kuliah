/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tugas_uas_smtr2;

import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author Ibnu Raditya
 */
public class Util {
    public static void tampilkanMapsPreview(JLabel label, double latitude, double longitude) {
    try {
        int width  = label.getWidth()  > 0 ? label.getWidth()  : 300;
        int height = label.getHeight() > 0 ? label.getHeight() : 200;

        // Fetch tile peta statis dari OpenStreetMap
       String mapUrl = String.format(
        java.util.Locale.US,
        "https://maps.geoapify.com/v1/staticmap?style=osm-bright&width=%d&height=%d&center=lonlat:%f,%f&zoom=15&marker=lonlat:%f,%f;color:%%23ff0000&apiKey=81908dc788f24704bd10093607d723a8",
        width, height, longitude, latitude, longitude, latitude
);

        java.awt.image.BufferedImage mapImg = javax.imageio.ImageIO.read(new java.net.URL(mapUrl));

        if (mapImg != null) {
            label.setIcon(new javax.swing.ImageIcon(mapImg));
            label.setText("");
        } else {
            label.setText("Preview peta gagal dimuat");
        }

        // Hapus listener lama agar tidak duplikat
        for (java.awt.event.MouseListener ml : label.getMouseListeners()) {
            label.removeMouseListener(ml);
        }

        // Klik → buka Google Maps di browser
        final double lat = latitude;
        final double lng = longitude;
        label.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        label.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                bukaMapsDiBrowser(
                    String.format(java.util.Locale.US,
                        "https://www.google.com/maps?q=%f,%f", lat, lng)
                );
            }
        });

    } catch (Exception e) {
        label.setText("Gagal memuat peta");
        label.setIcon(null);
        e.printStackTrace();
    }
}
 
    /**
     * Membuka URL Google Maps di browser default sistem.
     *
     * @param linkMaps URL Google Maps yang akan dibuka
     */
    public static void bukaMapsDiBrowser(String linkMaps) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI(linkMaps));
            } else {
                System.err.println("[Util] Desktop.browse tidak didukung di sistem ini.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
