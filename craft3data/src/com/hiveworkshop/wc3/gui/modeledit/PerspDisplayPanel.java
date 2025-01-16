package com.hiveworkshop.wc3.gui.modeledit;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.lwjgl.LWJGLException;

import com.hiveworkshop.wc3.gui.ProgramPreferences;
import com.hiveworkshop.wc3.mdl.Geoset;
import com.hiveworkshop.wc3.mdl.render3d.RenderModel;
import com.hiveworkshop.wc3.mdl.v2.ModelView;

import net.infonode.docking.View;

/**
 * Write a description of class DisplayPanel here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class PerspDisplayPanel extends JPanel {
    
    private final ModelView dispMDL;
    private PerspectiveViewport vp;
    private JPanel vpp;
    private String title;
    private final ProgramPreferences programPreferences;
    private final View view;
    private final RenderModel editorRenderModel;

    // private JCheckBox wireframe;
    public PerspDisplayPanel( final String title, final ModelView dispMDL, final ProgramPreferences programPreferences,
            final RenderModel editorRenderModel ) 
    {
        super();

        this.programPreferences = programPreferences;
        this.editorRenderModel = editorRenderModel;
        // BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(title),BorderFactory.createBevelBorder(1)),BorderFactory.createEmptyBorder(1,1,1,1)
        // ));
        setOpaque( true );

        // wireframe = new JCheckBox("Wireframe");
        // add(wireframe);
        setViewport( dispMDL );
        getViewport().setMinimumSize( new Dimension( 200, 200 ) );

        this.title = title;
        this.dispMDL = dispMDL;

        final GroupLayout layout = new GroupLayout( this );
        layout.setHorizontalGroup( layout.createSequentialGroup().addComponent( vp ) );
        layout.setVerticalGroup( layout.createParallelGroup( GroupLayout.Alignment.CENTER ).addComponent( vp ) );

        setLayout( new BorderLayout() );
        add( vp );

        this.view = new View( title, null, this );
    }

    public void setViewportBackground( final Color background ) {
        vp.setViewportBackground( background );
    }

    public Color getViewportBackground() {
        return vp.getBackground();
    }

    public View getView() {
        return view;
    }

    public void addGeosets( final List<Geoset> list ) {
        vp.addGeosets( list );
    }

    public void reloadTextures() {
        vp.reloadTextures();
    }

    public void reloadAllTextures() {
        vp.reloadAllTextures();
    }

    public void setViewport( final ModelView dispModel ) {
        setViewport( dispModel, 200 );
    }

    public void setViewport( final ModelView dispModel, final int viewerSize ) {
        try {
            if ( vp != null ) {
                vp.destroy();
            }
            removeAll();

            vp = new PerspectiveViewport( dispModel, programPreferences, editorRenderModel );
            vp.setIgnoreRepaint( false );
            vp.setMinimumSize( new Dimension( viewerSize, viewerSize ) );

            final GroupLayout layout = new GroupLayout( this );
            layout.setHorizontalGroup( layout.createSequentialGroup().addComponent( vp ) );
            layout.setVerticalGroup( layout.createParallelGroup( GroupLayout.Alignment.CENTER ).addComponent( vp ) );

            setLayout( new BorderLayout() );

        } 
        catch ( final LWJGLException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        add( vp, BorderLayout.CENTER );
    }

    public void setTitle( final String what ) {
        title = what;
        setBorder( BorderFactory.createTitledBorder( title ) );
    }

    public PerspectiveViewport getViewport() {
        return vp;
    }

    @Override
    public void paintComponent( final Graphics g ) {
        super.paintComponent( g );

        vp.paint( vp.getGraphics() );
    }

    // public void addGeoset(Geoset g)
    // {
    // m_geosets.add(g);
    // }
    // public void setGeosetVisible(int index, boolean flag)
    // {
    // Geoset geo = (Geoset)m_geosets.get(index);
    // geo.setVisible(flag);
    // }
    // public void setGeosetHighlight(int index, boolean flag)
    // {
    // Geoset geo = (Geoset)m_geosets.get(index);
    // geo.setHighlight(flag);
    // }
    // public void clearGeosets()
    // {
    // m_geosets.clear();
    // }
    // public int getGeosetsSize()
    // {
    // return m_geosets.size();
    // }

    public ImageIcon getImageIcon() {
        return new ImageIcon( vp.getBufferedImage() );
    }

    public BufferedImage getBufferedImage() {
        return vp.getBufferedImage();
    }
}
