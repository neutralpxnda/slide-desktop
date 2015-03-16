package gui

import java.awt.Color
import java.awt.event.{ActionListener, KeyEvent}
import javax.swing._

import connections.usb.Adb
import davinci.Device
import net.miginfocom.swing.MigLayout

class DeviceField(val posX: Int, val posY: Int, var actionListener: ActionListener) extends JPanel {

    this.setLayout(new MigLayout())

    private val searchingText: String =
        "<html><center>No devices detected<br/><br/>Scanning for devices...</center></html>"

    private var ip: String = "localhost"
    private var name: String = ""
    private var manufacturer: String = ""

    private val connectButton: JButton = new JButton("Connect")
    connectButton.setEnabled(false)

    private val nameLabel: JLabel = new JLabel
    private val manuLabel: JLabel = new JLabel
    private val ipAddressLabel: JLabel = new JLabel

    private val lblIcon: JLabel = new JLabel(searchingText)
    this.lblIcon.setHorizontalAlignment(SwingConstants.CENTER)

    this.add(lblIcon, "cell 0 0, growy, growx")
    this.add(manuLabel, "cell 0 1, growx")
    this.add(nameLabel, "cell 0 2, growx")
    this.add(ipAddressLabel, "cell 0 3, growx")
    this.add(connectButton, "cell 0 4, w 150!, growx")

    val alta: KeyBinder = new KeyBinder(KeyEvent.VK_ALT, KeyEvent.VK_A) {
        override def onKeysDown(): Unit = Adb.showAdbDevices()
    }
    val altl: KeyBinder = new KeyBinder(KeyEvent.VK_ALT, KeyEvent.VK_L) {
        override def onKeysDown(): Unit = Licence.showLicense()
    }

    this.addKeyListener(alta)
    this.addKeyListener(altl)

    this.setVisible(true)

    def setUi(d: Device): Unit = {
        val icon: ImageIcon = d.icon
        lblIcon.setIcon(icon)
        lblIcon.setText("")

        if (d.ip != "USB") {
            ip = d.ip
            name = d.model
            manufacturer = d.manufacturer
        }
        setComponents()
    }

    private def setComponents() {
        lblIcon.setHorizontalAlignment(SwingConstants.CENTER)

        nameLabel.setText(this.name)
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER)
        nameLabel.setBackground(Color.white)

        manuLabel.setText(manufacturer)
        manuLabel.setBackground(Color.white)
        manuLabel.setHorizontalAlignment(SwingConstants.CENTER)

        ipAddressLabel.setText(ip)
        ipAddressLabel.setBackground(Color.white)
        ipAddressLabel.setHorizontalAlignment(SwingConstants.CENTER)

        connectButton.setHorizontalAlignment(SwingConstants.CENTER)

        setListeners()
        this.updateUI()
    }

    def setListeners() {
        if (connectButton.getActionListeners.length <= 0) {
            connectButton.addActionListener(actionListener)
        }
    }

    def maxWidth(): Int = connectButton.getX + connectButton.getWidth + 10
    def maxHeight(): Int = connectButton.getY + connectButton.getHeight + 10

    override def show(): Unit = { showDeviceField(visibility = true) }
    def showDeviceField(visibility: Boolean): Unit = {
        if (visibility) {
            this.lblIcon.setText("")
        } else {
            this.lblIcon.setText(searchingText)
            this.lblIcon.setIcon(null)
        }

        this.nameLabel.setVisible(visibility)
        this.nameLabel.setText("")

        this.manuLabel.setVisible(visibility)
        this.manuLabel.setText("")

        this.ipAddressLabel.setVisible(visibility)
        this.ipAddressLabel.setText("localhost")

        this.connectButton.setEnabled(visibility)
    }
}