package net.minecraft.src;

import java.util.List;
import net.minecraft.client.Minecraft;

public class GuiIngameMenu extends GuiScreen
{
    private int updateCounter2;
    private int updateCounter;

    /* WORLD DOWNLOADER ---> */
    private int stopDownloadIn = -1;
    /* <--- WORLD DOWNLOADER */
    

    public GuiIngameMenu()
    {
        updateCounter2 = 0;
        updateCounter = 0;
    }

    public void initGui()
    {
        updateCounter2 = 0;
        controlList.clear();
        byte byte0 = -16;
        controlList.add(new GuiButton(1, width / 2 - 100, height / 4 + 120 + byte0, StatCollector.translateToLocal("menu.returnToMenu")));
        if (mc.isMultiplayerWorld())
        {
            ((GuiButton)controlList.get(0)).displayString = StatCollector.translateToLocal("menu.disconnect");
            /* WORLD DOWNLOADER ---> */
            ((GuiButton)controlList.get(0)).yPosition = height / 4 + 144 + byte0;
            if( WorldDL.downloading == false )
            	//controlList.add(new GuiButton(7, width / 2 - 100, height / 4 + 120 + byte0, 170, 20, "Download this world"));
            	controlList.add(new GuiButton(7, width / 2 - 100, height / 4 + 120 + byte0, 200, 20, "Download this world"));
            else
            	//controlList.add(new GuiButton(7, width / 2 - 100, height / 4 + 120 + byte0, 170, 20, "Stop download"));
            	controlList.add(new GuiButton(7, width / 2 - 100, height / 4 + 120 + byte0, 200, 20, "Stop download"));
            
            //controlList.add(new GuiButton(8, width / 2 + 71, height / 4 + 120 + byte0, 28, 20, "..."));
            /* <--- WORLD DOWNLOADER */

        }
        controlList.add(new GuiButton(4, width / 2 - 100, height / 4 + 24 + byte0, StatCollector.translateToLocal("menu.returnToGame")));
        controlList.add(new GuiButton(0, width / 2 - 100, height / 4 + 96 + byte0, StatCollector.translateToLocal("menu.options")));
        controlList.add(new GuiButton(5, width / 2 - 100, height / 4 + 48 + byte0, 98, 20, StatCollector.translateToLocal("gui.achievements")));
        controlList.add(new GuiButton(6, width / 2 + 2, height / 4 + 48 + byte0, 98, 20, StatCollector.translateToLocal("gui.stats")));
    }

    protected void actionPerformed(GuiButton guibutton)
    {
        if (guibutton.id == 0)
        {
            mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
        }
        if (guibutton.id == 1)
        {
            mc.statFileWriter.readStat(StatList.leaveGameStat, 1);
            if (mc.isMultiplayerWorld())
            {
                /* WORLD DOWNLOADER ---> */
                if( WorldDL.downloading == true )
                    WorldDL.stopDownload();
                /* <--- WORLD DOWNLOADER */
                mc.theWorld.sendQuittingDisconnectingPacket();
            }
            mc.changeWorld1(null);
            mc.displayGuiScreen(new GuiMainMenu());
        }
        if (guibutton.id == 4)
        {
            mc.displayGuiScreen(null);
            mc.setIngameFocus();
        }
        if (guibutton.id == 5)
        {
            mc.displayGuiScreen(new GuiAchievements(mc.statFileWriter));
        }
        if (guibutton.id == 6)
        {
            mc.displayGuiScreen(new GuiStats(this, mc.statFileWriter));
        }
        /* WORLD DOWNLOADER ---> */
        if(guibutton.id == 7)
        {
        	WorldDL.mc = mc;
        	WorldDL.wc = (WorldClient)mc.theWorld;
			if( WorldDL.downloading == true )
			{
				((GuiButton)controlList.get(1)).displayString = "Saving a shitload of data...";
				stopDownloadIn = 2;
			}
			else
			{
				WorldDL.startDownload();
				mc.displayGuiScreen(null);
				mc.setIngameFocus();
			}
        }
        //if(guibutton.id == 8)
        //    mc.displayGuiScreen( new GuiWorldDownloader( this ) );
        /* <--- WORLD DOWNLOADER */

    }

    public void updateScreen()
    {
        super.updateScreen();
        updateCounter++;
    }

    public void drawScreen(int i, int j, float f)
    {
        drawDefaultBackground();
        boolean flag = !mc.theWorld.quickSaveWorld(updateCounter2++);
        if (flag || updateCounter < 20)
        {
            float f1 = ((float)(updateCounter % 10) + f) / 10F;
            f1 = MathHelper.sin(f1 * 3.141593F * 2.0F) * 0.2F + 0.8F;
            int k = (int)(255F * f1);
            drawString(fontRenderer, "Saving level..", 8, height - 16, k << 16 | k << 8 | k);
        }
        drawCenteredString(fontRenderer, "Game menu", width / 2, 40, 0xffffff);
        super.drawScreen(i, j, f);
        /* WORLD DOWNLOADER ---> */
        if( stopDownloadIn == 0 )
        {
            WorldDL.stopDownload();
            mc.displayGuiScreen(null);
            mc.setIngameFocus();
        }
        else if( stopDownloadIn > 0 )
            stopDownloadIn--;
        /* <--- WORLD DOWNLOADER */
    }
}
